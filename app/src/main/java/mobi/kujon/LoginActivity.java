package mobi.kujon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN = 1;
    private GoogleApiClient apiClient;

    @Bind(R.id.sign_in_button) SignInButton signInButton;
    @Bind(R.id.logout) Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.serverClientId))
                .requestProfile()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        checkLoggingStatus();
    }

    private void checkLoggingStatus() {
        OptionalPendingResult<GoogleSignInResult> silentSignIn = Auth.GoogleSignInApi.silentSignIn(apiClient);
        if (silentSignIn.isDone()) {
            loginUi(true);
            handle(silentSignIn.get());
        } else {
            silentSignIn.setResultCallback(result -> {
                loginUi(result.isSuccess());
                handle(result);
            });
        }
    }

    private void handle(GoogleSignInResult result) {
        System.out.println("result = [" + result + "]");
        loginUi(result.isSuccess());
        if (!result.isSuccess()) {
            System.out.println("######### unsuccessfull login");
            return;
        }
        System.out.println("########### " + result.getSignInAccount().getEmail());
        System.out.println("result token = [" + result.getSignInAccount().getIdToken() + "]");
        logOutButton.setText("Logout: " + result.getSignInAccount().getEmail());
    }

    private void loginUi(boolean loggedIn) {
        logOutButton.setVisibility(loggedIn ? View.VISIBLE : View.GONE);
        signInButton.setVisibility(loggedIn ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.logout)
    public void click() {
        Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(status -> checkLoggingStatus());
    }

    @OnClick(R.id.sign_in_button)
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handle(result);
        }
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }
}
