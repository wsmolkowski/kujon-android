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

    @Bind(R.id.webView) WebView webView;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    OkHttpClient client = new OkHttpClient();

    Gson gson = new Gson();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usos_login);
        ButterKnife.bind(this);

        String usosId = getIntent().getStringExtra(USOS_ID);
        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override public void onPageFinished(WebView view, String url) {
                System.out.println("### Finished loading: " + url);
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("### Trying to load " + url);
                if (url.contains("https://api.kujon.mobi/authentication/verifymobi")) {

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            Toast.makeText(UsoswebLoginActivity.this, "Network rrror", Toast.LENGTH_SHORT).show();
                            Crashlytics.logException(e);
                            finish();
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            String responseString = response.body().string();
                            System.out.println("#### " + responseString);
                            KujonResponse kujonResponse = gson.fromJson(responseString, KujonResponse.class);
                            if ("success".equals(kujonResponse.status)) {
                                runOnUiThread(() -> {
                                    Toast.makeText(UsoswebLoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UsoswebLoginActivity.this, UserProfileActivity.class));
                                });
                            } else {
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
        System.out.println("url = " + url);
        webView.loadUrl(url);
    }
}
