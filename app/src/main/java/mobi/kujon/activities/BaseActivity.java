package mobi.kujon.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.utils.KujonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.createChooser;


public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BaseActivity";

    @Inject protected KujonBackendApi kujonBackendApi;
    @Inject protected KujonApplication kujonApplication;
    @Inject protected Picasso picasso;
    @Inject protected KujonUtils utils;

    protected GoogleApiClient apiClient;
    private AlertDialog alertDialog;
    private ProgressBar progressBar;
    private FrameLayout content;
    protected Gson gson = new Gson();
    protected Handler handler = new Handler();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KujonApplication.getComponent().inject(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.serverClientId))
                .requestProfile()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(layoutParams);
        content = (FrameLayout) findViewById(android.R.id.content);
    }

    public void showProgress(boolean show) {
        if (show) {
            content.removeView(progressBar);
            content.addView(progressBar);
        } else {
            content.removeView(progressBar);
        }
    }

    @Override protected void onStart() {
        super.onStart();
        checkLoggingStatus(this::handle);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(this.getClass().getSimpleName()));
    }

    @Override protected void onRestart() {
        super.onRestart();
        checkLoggingStatus(this::handle);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return true;
        }
    }

    protected void contactUs() {
        String uriText = String.format("mailto:%s?subject=%s&body=%s",
                getString(R.string.contact_email),
                Uri.encode(getString(R.string.email_subject)),
                Uri.encode(getString(R.string.email_body)));

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse(uriText));
        startActivity(createChooser(sendIntent, "Select an email client"));
    }

    protected void deleteAccount() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        String email = KujonApplication.getApplication().getLoginStatus().getSignInAccount().getEmail();
        dlgAlert.setMessage(Html.fromHtml(getString(R.string.delete_confirm, email)));
        dlgAlert.setTitle(R.string.delete_account);
        dlgAlert.setPositiveButton("OK", (dialog, which) -> {
            kujonBackendApi.deleteAccount().enqueue(new Callback<Object>() {
                @Override public void onResponse(Call<Object> call, Response<Object> response) {
                    System.out.println("call = [" + call + "], response = [" + response + "]");
                    logout();
                }

                @Override public void onFailure(Call<Object> call, Throwable t) {
                    Crashlytics.logException(t);
                    logout();
                }
            });
        });
        dlgAlert.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.dismiss();
        });
        dlgAlert.setCancelable(false);
        alertDialog = dlgAlert.create();
        alertDialog.show();
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
            if (this instanceof UsosesActivity || this instanceof MainActivity) {
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    public void logout() {
        Log.d(TAG, "logout() called with: " + "");
        Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(status -> checkLoggingStatus(this::handle));
        utils.clearCache();
        kujonApplication.finishAllAcitities();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }
}
