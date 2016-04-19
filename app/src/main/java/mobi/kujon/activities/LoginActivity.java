package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;


public class LoginActivity extends BaseActivity {

    public static final int RC_SIGN_IN = 1;

    @Bind(R.id.sign_in_button) SignInButton signIn;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
            handle(result);
        }
    }

    @Override public void handle(GoogleSignInResult result) {
        if (result.isSuccess()) {
            signIn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            KujonApplication.getApplication().setLoginStatus(result);
            // FIXME
            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
            finish();
//            kujonBackendApi.config().enqueue(new Callback<KujonResponse<Config>>() {
//                @Override public void onResponse(Call<KujonResponse<Config>> call, Response<KujonResponse<Config>> response) {
//                    Config data = response.body().data;
//                    System.out.println("### " + data);
//                    if (data.usosPaired) {
//                        startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
//                    } else {
//                        startActivity(new Intent(LoginActivity.this, UsosesActivity.class));
//                    }
//                    finish();
//                }
//
//                @Override public void onFailure(Call<KujonResponse<Config>> call, Throwable t) {
//                    Crashlytics.logException(t);
//                }
//            });
        }
    }
}