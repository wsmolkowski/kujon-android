package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.json.Config;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.KujonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity {

    @Inject KujonUtils utils;

    private static final Logger log = LoggerFactory.getLogger(LoginActivity.class);

    private static final String TAG = "LoginActivity";

    public static final int RC_SIGN_IN = 1;

    @Bind(R.id.sign_in_button) SignInButton signIn;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.regulations) TextView regulations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(R.string.main_login_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        KujonApplication.getComponent().inject(this);

        regulations.setText(Html.fromHtml(getString(R.string.regulations_info)));
    }

    @Override protected void onStart() {
        super.onStart();
        progress(true);
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progress(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            progress(false);
            if (resultCode == RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handle(result);
                return;
            } else {
                log.error(String.format("Login error requestCode=%s, resultCode=%s, data=%s", requestCode, resultCode, data));
                Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override public void handle(GoogleSignInResult result) {
        if (result.isSuccess()) {
            log.info("handle: Login successfull. Checking usos paired status");
            progress(true);
            KujonApplication.getApplication().setLoginStatus(result);
            utils.clearCache();
            kujonBackendApi.config().enqueue(new Callback<KujonResponse<Config>>() {
                @Override public void onResponse(Call<KujonResponse<Config>> call, Response<KujonResponse<Config>> response) {
                    if (ErrorHandlerUtil.handleResponse(response)) {
                        Config data = response.body().data;
                        log.debug("Response: " + data);
                        if (data.usosPaired) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, UsosesActivity.class));
                        }
                        finish();
                    }
                }

                @Override public void onFailure(Call<KujonResponse<Config>> call, Throwable t) {
                    ErrorHandlerUtil.handleError(t);
                }
            });
        } else {
            progress(false);
            Log.i(TAG, "Login not successful: " + result.getStatus().getStatusMessage());
        }
    }

    private void progress(boolean show) {
        signIn.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.regulations)
    public void regulations(){
        String url = getString(R.string.regulations_url);
        WebViewAcitivty.showUrl(this, url);
    }
}
