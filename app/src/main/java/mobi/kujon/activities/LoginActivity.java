package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.ApiProvider;
import mobi.kujon.network.ApiType;
import mobi.kujon.network.json.Config;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.KujonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity {

    private static final int DELAY_MILLIS = 3000;
    @Inject
    KujonUtils utils;
    @Inject
    ApiProvider apiProvider;



    private static final String TAG = "LoginActivity";

    public static final int RC_SIGN_IN = 1;

    @BindView(R.id.google_login_button)
    View signIn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.regulations)
    TextView regulations;
    @BindView(R.id.login_text)
    TextView logInByText;

    @BindView(R.id.login_logo)
    ImageView loginLogo;
    private int counter = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        KujonApplication.getComponent().inject(this);
        ((KujonApplication)this.getApplication()).resetFilesComponent();
        regulations.setText(Html.fromHtml(getString(R.string.regulations_info)));
        setChangeApi();
    }

    private void setChangeApi() {
        loginLogo.setOnClickListener(view -> {
            counter++;
            if (counter != 5) {
                startDelayInZeroingCounter();
                return;
            }
            apiProvider.switchApiType();
            utils.clearCache();
            switch (apiProvider.getApiType()) {
                case ApiType.DEMO:
                    Toast.makeText(LoginActivity.this, R.string.change_api_demo, Toast.LENGTH_SHORT).show();
                    break;
                case ApiType.PROD:
                    Toast.makeText(LoginActivity.this, R.string.change_api_prod, Toast.LENGTH_SHORT).show();
                    break;
            }

            KujonApplication.getComponent().inject(LoginActivity.this);
            counter = 0;
        });
    }


    private void startDelayInZeroingCounter() {
        if (handler == null) {
            handler = new Handler();
        }
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> counter = 0, DELAY_MILLIS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progress(true);
    }

    @OnClick(R.id.google_login_button)
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progress(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == RC_SIGN_IN) {
                progress(false);
                if (resultCode == RESULT_OK) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handle(result);
                } else {
                    progress(false);
                    Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            progress(false);
        }
    }

    @Override
    public void handle(GoogleSignInResult result) {
        if (result.isSuccess()) {
            progress(true);

            KujonApplication.getApplication().setLoginStatus(result);
            utils.clearCache();
            kujonBackendApi.config().enqueue(new Callback<KujonResponse<Config>>() {
                @Override
                public void onResponse(Call<KujonResponse<Config>> call, Response<KujonResponse<Config>> response) {
                    Integer code = utils.getResponseCode(response);
                    if (code == null) {
                        Toast.makeText(LoginActivity.this, R.string.server_communication_error, Toast.LENGTH_SHORT).show();
                        progress(false);
                    } else if (code == 401) {
                        handleLoginFailure();
                        progress(false);
                    } else {
                        proceedNormalResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<KujonResponse<Config>> call, Throwable t) {
                    ErrorHandlerUtil.handleError(t);
                    progress(false);
                }
            });
        } else {
            progress(false);
            Log.i(TAG, "Login not successful: " + result.getStatus().getStatusMessage());
        }
    }

    private void handleLoginFailure() {
        Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
        Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(status -> {
            progress(false);
            Toast.makeText(LoginActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void proceedNormalResponse(Response<KujonResponse<Config>> response) {
        if (ErrorHandlerUtil.handleResponse(response)) {

            Config data = response.body().data;

            if (data.usosPaired) {
                if (data.usosWorks) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    ErrorActivity.open(LoginActivity.this);
                }
            } else {
                startActivity(new Intent(LoginActivity.this, UsosesActivity.class));
            }
            finish();
        }
    }

    private void progress(boolean show) {
        signIn.setVisibility(show ? View.GONE : View.VISIBLE);
        logInByText.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.regulations)
    public void regulations() {
        String url = getString(R.string.regulations_url);
        WebViewAcitivty.showUrl(this, url);
    }
}
