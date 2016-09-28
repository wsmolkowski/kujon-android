package mobi.kujon.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
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
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Usos;
import mobi.kujon.utils.ErrorHandlerUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static mobi.kujon.BuildConfig.API_URL;

public class UsoswebLoginActivity extends BaseActivity {

    private static final String TAG = "UsoswebLoginActivity";

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

            @Override public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d(TAG, "onReceivedError() called with: " + "view = [" + view + "], request = [" + request + "], error = [" + error + "]");
            }

            @Override public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.d(TAG, "onReceivedHttpError() called with: " + "view = [" + view + "], request = [" + request + "], errorResponse = [" + errorResponse + "]");
            }

            @Override public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.d(TAG, "onReceivedSslError() called with: " + "view = [" + view + "], handler = [" + handler + "], error = [" + error + "]");
            }

            @Override public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
                super.onTooManyRedirects(view, cancelMsg, continueMsg);
                Log.d(TAG, "onTooManyRedirects() called with: " + "view = [" + view + "], cancelMsg = [" + cancelMsg + "], continueMsg = [" + continueMsg + "]");
            }

            @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.d(TAG, "onReceivedError() called with: " + "view = [" + view + "], errorCode = [" + errorCode + "], description = [" + description + "], failingUrl = [" + failingUrl + "]");
            }


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
                if (url.contains(API_URL + "authentication/verify")) {
                    log.info("Got URL to kujon. Making request: " + url);

                    String cookie = CookieManager.getInstance().getCookie(API_URL);

                    Request.Builder builder = new Request.Builder().url(url);
                    if (cookie != null) builder.addHeader("Cookie", cookie);
                    Request request = builder.build();

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
            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String url = String.format(API_URL + "authentication/register?email=%s&token=%s&usos_id=%s&type=GOOGLE&device_type=ANDROID&device_id=%s",
                    account.getEmail(), account.getIdToken(), usos.usosId, deviceId);
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
