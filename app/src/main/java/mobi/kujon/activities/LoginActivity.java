package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ResultCallback;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.KujonBackendService;
import mobi.kujon.network.json.Config;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity implements ResultCallback<GoogleSignInResult> {

    public static final int RC_SIGN_IN = 1;

    private KujonBackendApi kujonBackendApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        kujonBackendApi = KujonBackendService.getInstance().getKujonBackendApi();
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            onResult(result);
        }
    }

    @Override public void handle(GoogleSignInResult result) {
        onResult(result);
    }

    @Override public void onResult(@NonNull GoogleSignInResult result) {
        if (result.isSuccess()) {
            kujonBackendApi.config().enqueue(new Callback<KujonResponse<Config>>() {
                @Override public void onResponse(Call<KujonResponse<Config>> call, Response<KujonResponse<Config>> response) {
                    Config data = response.body().data;
                    if (data.usosPaired) {
                        Toast.makeText(LoginActivity.this, "Will shou user data", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(LoginActivity.this, UsosesActivity.class));
                    }
                    finish();
                }

                @Override public void onFailure(Call<KujonResponse<Config>> call, Throwable t) {
                    Crashlytics.logException(t);
                }
            });

        }
    }
}
