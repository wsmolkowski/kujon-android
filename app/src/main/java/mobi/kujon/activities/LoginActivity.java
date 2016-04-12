package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ResultCallback;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;


public class LoginActivity extends BaseActivity implements ResultCallback<GoogleSignInResult> {

    public static final int RC_SIGN_IN = 1;

    public static Class AFTER_LOGIN_ACTIVITY = UsosesActivity.class;

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
            onResult(result);
        }
    }

    @Override public void onResult(@NonNull GoogleSignInResult result) {
        if (result.isSuccess()) {
            startActivity(new Intent(this, AFTER_LOGIN_ACTIVITY));
            finish();
        }
    }
}
