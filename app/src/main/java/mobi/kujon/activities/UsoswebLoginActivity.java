package mobi.kujon.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsoswebLoginActivity extends BaseActivity {

    public static final String USOS_ID = "USOS_ID";
    public static final Logger log = LoggerFactory.getLogger(UsoswebLoginActivity.class);

    @Bind(R.id.webView) WebView webView;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    OkHttpClient client = new OkHttpClient();

    Gson gson = new Gson();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        log.info("UsoswebLoginActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usos_login);
        ButterKnife.bind(this);

        String usosId = getIntent().getStringExtra(USOS_ID);
        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                log.info("onPageStarted: " + url);
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override public void onPageFinished(WebView view, String url) {
                log.debug("onPageFinished: " + url);
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                log.info("shouldOverrideUrlLoading: " + url);
                if (url.contains("https://api.kujon.mobi/authentication/verifymobi")) {
                    log.info("Got URL to kujon. Making request: " + url);

                    Request request = new Request.Builder()
                            .url(url)
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
                            if (kujonResponse.isSuccessful()) {
                                runOnUiThread(() -> {
                                    Toast.makeText(UsoswebLoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UsoswebLoginActivity.this, CongratulationsActivity.class));
                                });
                            } else {
                                log.error("Login error");
                                Toast.makeText(UsoswebLoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }
                    });

                    return true;
                }
                return false;
            }
        });
        GoogleSignInResult loginStatus = KujonApplication.getApplication().getLoginStatus();
        GoogleSignInAccount account = loginStatus.getSignInAccount();
        String url = String.format("https:/api.kujon.mobi/authentication/mobi?email=%s&token=%s&usos_id=%s", account.getEmail(), account.getIdToken(), usosId);
        log.info("Loading urs: " + url);
        webView.loadUrl(url);
    }
}
