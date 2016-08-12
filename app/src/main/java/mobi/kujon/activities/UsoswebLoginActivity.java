package mobi.kujon.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.NetModule;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Usos;
import mobi.kujon.utils.ErrorHandlerUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsoswebLoginActivity extends BaseActivity {

    public static final String USOS_POJO = "USOS_POJO";
    public static final Logger log = LoggerFactory.getLogger(UsoswebLoginActivity.class);

    @Bind(R.id.webView) WebView webView;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    @Inject OkHttpClient client;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log.info("UsoswebLoginActivity");
        KujonApplication.getComponent().inject(this);
        CookieManager.getInstance().setAcceptCookie(true);
        setContentView(R.layout.activity_usos_login);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(R.string.login_title);

        String usosPojo = getIntent().getStringExtra(USOS_POJO);
        Usos usos = gson.fromJson(usosPojo, Usos.class);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this, webView), "HtmlViewer");
        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                log.info("onPageStarted: " + url);
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override public void onPageFinished(WebView view, String url) {
                log.debug("onPageFinished: " + url);
                progressBar.setVisibility(View.GONE);
                webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                super.onPageFinished(view, url);
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                log.info("shouldOverrideUrlLoading: " + url);
                if (url.contains("https://api.kujon.mobi/authentication/verify")) {
                    log.info("Got URL to kujon. Making request: " + url);

                    String cookie = CookieManager.getInstance().getCookie(NetModule.API_URL);

                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Cookie", cookie)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            Toast.makeText(UsoswebLoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            Crashlytics.logException(e);
                            log.error("Network error:" + e.getMessage());
                            finish();
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            String responseString = response.body().string();
                            log.debug("Got response from server: " + responseString);
                            KujonResponse kujonResponse = gson.fromJson(responseString, KujonResponse.class);
                            runOnUiThread(() -> {
                                if (kujonResponse.isSuccessful()) {
                                    Toast.makeText(UsoswebLoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                                    Intent data = new Intent();
                                    data.putExtra(USOS_POJO, usosPojo);
                                    setResult(RESULT_OK, data);
                                    finish();

                                } else {
                                    log.error("Login error");
                                    Toast.makeText(UsoswebLoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    });

                    return true;
                }
                return false;
            }
        });
        KujonApplication.getApplication().getLoginStatus().onSuccessTask(task -> {
            GoogleSignInResult signInResult = task.getResult();
            GoogleSignInAccount account = signInResult.getSignInAccount();
            String url = String.format("https:/api.kujon.mobi/authentication/register?email=%s&token=%s&usos_id=%s&type=GOOGLE", account.getEmail(), account.getIdToken(), usos.usosId);
            log.info("Loading urs: " + url);
            webView.loadUrl(url);
            return null;
        }, Task.UI_THREAD_EXECUTOR).continueWith(ErrorHandlerUtil.ERROR_HANDLER);

    }

    class MyJavaScriptInterface {

        private Context ctx;
        private WebView webView;

        MyJavaScriptInterface(Context ctx, WebView webView) {
            this.ctx = ctx;
            this.webView = webView;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            if (html.contains("\"status\": \"fail\"")) {
                Pattern pattern = Pattern.compile("\"message\": \"(.*?)\"");
                Matcher matcher = pattern.matcher(html);
                String errorMessage = matcher.find() ? matcher.group(1) : "Wystąpił błąd";
                String message = StringEscapeUtils.unescapeJava(errorMessage);
                runOnUiThread(() -> webView.loadData(message, "text/html; charset=UTF-8", null));
            }
        }

    }

}
