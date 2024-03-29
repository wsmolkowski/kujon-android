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

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import bolts.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.ApiProvider;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Usos;
import mobi.kujon.utils.ErrorHandlerUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsoswebLoginActivity extends BaseActivity {

    private static final String TAG = "UsoswebLoginActivity";

    public static final String USOS_POJO = "USOS_POJO";


    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Inject OkHttpClient client;
    @Inject ApiProvider apiProvider;

    private String API_URL;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KujonApplication.getComponent().inject(this);
        CookieManager.getInstance().setAcceptCookie(true);
        setContentView(R.layout.activity_usos_login);
        API_URL = apiProvider.getApiURL();
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

                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override public void onPageFinished(WebView view, String url) {

                progressBar.setVisibility(View.GONE);
                webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                super.onPageFinished(view, url);
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains(API_URL + "authentication/verify")) {


                    String cookie = CookieManager.getInstance().getCookie(API_URL);

                    Request.Builder builder = new Request.Builder().url(url);
                    if (cookie != null) builder.addHeader("Cookie", cookie);
                    Request request = builder.build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(UsoswebLoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();

                                finish();
                            });
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            String responseString = response.body().string();
                            try {
                                KujonResponse kujonResponse = gson.fromJson(responseString, KujonResponse.class);
                                runOnUiThread(() -> {
                                    if (kujonResponse.isSuccessful()) {
                                        Toast.makeText(UsoswebLoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                                        Intent data = new Intent();
                                        data.putExtra(USOS_POJO, usosPojo);
                                        setResult(RESULT_OK, data);
                                        finish();
                                    } else {

                                        Toast.makeText(UsoswebLoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }catch (JsonSyntaxException jse){
                                runOnUiThread(() -> {
                                    Toast.makeText(UsoswebLoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();

                                    finish();
                                });
                            }
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
