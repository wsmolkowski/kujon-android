package mobi.kujon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.KujonBackendService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.createChooser;


public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BaseActivity";

    protected SharedPreferences kujonPreferences;
    protected GoogleApiClient apiClient;
    protected KujonBackendApi kujonBackendApi;
    protected Picasso picasso;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kujonPreferences = getSharedPreferences("kujon_preferences", MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.serverClientId))
                .requestProfile()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        kujonBackendApi = KujonBackendService.getInstance().getKujonBackendApi();

        picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(KujonBackendService.getInstance().getHttpClient()))
                .build();
    }

    @Override protected void onStart() {
        super.onStart();
        checkLoggingStatus(this::handle);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.logout).setVisible(!(this instanceof LoginActivity));
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.delete_account:
                kujonBackendApi.deleteAccount().enqueue(new Callback() {
                    @Override public void onResponse(Call call, Response response) {
                        System.out.println("call = [" + call + "], response = [" + response + "]");
                        logout();
                    }

                    @Override public void onFailure(Call call, Throwable t) {
                        Crashlytics.logException(t);
                        logout();
                    }
                });
                return true;

            case R.id.contact_us:
                String uriText = String.format("mailto:%s?subject=%s&body=%s",
                        getString(R.string.contact_email),
                        Uri.encode("Kujon Team - Aplikacja Android"),
                        Uri.encode("Prześlij nam swój komentarz lub opinię\n\nKujon Team"));

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse(uriText));
                startActivity(createChooser(sendIntent, "Select an email client"));
                return true;

            default:
                return true;
        }
    }

    public void checkLoggingStatus(ResultCallback<GoogleSignInResult> resultCallback) {
        Log.i(TAG, "checkLoggingStatus: " + this.getClass().getSimpleName());
        OptionalPendingResult<GoogleSignInResult> silentSignIn = Auth.GoogleSignInApi.silentSignIn(apiClient);
        if (silentSignIn.isDone()) {
            resultCallback.onResult(silentSignIn.get());
        } else {
            silentSignIn.setResultCallback(resultCallback);
        }
    }

    public void handle(GoogleSignInResult result) {
        Log.i(TAG, "handle sign in status: " + result.isSuccess());
        KujonApplication.getApplication().setLoginStatus(result);
        if (!result.isSuccess() && !(this instanceof LoginActivity)) {
            finish();
            if (this instanceof UsosesActivity || this instanceof UserProfileActivity) {
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    public void logout() {
        Log.d(TAG, "logout() called with: " + "");
        Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(status -> checkLoggingStatus(this::handle));
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }
}