package mobi.kujon.activities;

import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import bolts.Task;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.SettingsApi;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.KujonUtils;
import mobi.kujon.utils.lang_force.ForceLanguage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.Intent.createChooser;


public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "BaseActivity";

    @Inject protected KujonBackendApi kujonBackendApi;
    @Inject protected SettingsApi settingsApi;
    @Inject protected KujonApplication kujonApplication;
    @Inject protected Picasso picasso;
    @Inject protected KujonUtils utils;
    @Inject protected ForceLanguage forceLanguage;

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
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
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

    @Override
    protected void onResume() {
        super.onResume();
        forceLanguage.setLocale(this);
    }

    @Override protected void onPostResume(){
        super.onPostResume();
        setBack();
    }

    private void setBack() {
        View backView = findViewById(R.id.back);
        if (backView != null) backView.setOnClickListener(v -> onBackPressed());
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
        if (!apiClient.isConnected()) {
            apiClient.connect();
        }
        checkLoggingStatus(this::handle);
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(this.getClass().getSimpleName()));
    }

    @Override protected void onRestart() {
        super.onRestart();
        checkLoggingStatus(this::handle);
    }

    @Override protected void onStop() {
        super.onStop();
        if (apiClient.isConnected()) {
            apiClient.disconnect();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        cancelLastCallIfExist();
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
                return super.onOptionsItemSelected(item);
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
        KujonApplication.getApplication().getLoginStatus().onSuccessTask(task -> {
            GoogleSignInResult signInResult = task.getResult();
            String email = signInResult.getSignInAccount().getEmail();
            dlgAlert.setMessage(Html.fromHtml(getString(R.string.delete_confirm, email)));
            dlgAlert.setTitle(R.string.delete_account);
            dlgAlert.setPositiveButton("OK", (dialog, which) -> {
                kujonBackendApi.deleteAccount().enqueue(new Callback<Object>() {
                    @Override public void onResponse(Call<Object> call, Response<Object> response) {
                        System.out.println("backendCall = [" + call + "], response = [" + response + "]");
                        googleLogout();
                    }

                    @Override public void onFailure(Call<Object> call, Throwable t) {
                        googleLogout();
                    }
                });
            });
            dlgAlert.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                dialog.dismiss();
            });
            dlgAlert.setCancelable(false);
            alertDialog = dlgAlert.create();
            alertDialog.show();
            return null;
        }, Task.UI_THREAD_EXECUTOR).continueWith(ErrorHandlerUtil.ERROR_HANDLER);

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
        if (!result.isSuccess()) {
//            OneSignal.deleteTag(KujonApplication.USER_EMAIL_TAG);
        }
        if (!result.isSuccess() && !(this instanceof LoginActivity)) {
            finish();
            if (this instanceof UsosesActivity || this instanceof MainActivity) {
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    public void logout() {
        Log.d(TAG, "logout() called with: " + "");
        showProgress(true);
        kujonBackendApi.logout().enqueue(new Callback<KujonResponse<String>>() {
            @Override
            public void onResponse(Call<KujonResponse<String>> call, Response<KujonResponse<String>> response) {
                showProgress(false);
                if(ErrorHandlerUtil.handleResponse(response)) {
                    googleLogout();
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<String>> call, Throwable t) {
                showProgress(false);
            }
        });
    }
    private boolean googleToLogout = false;
    private void googleLogout() {
        if(apiClient.isConnected()){
            performGoogleLogout();
        }else {
            apiClient.connect();
            googleToLogout = true;
        }
    }

    private void performGoogleLogout() {
        Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(status -> checkLoggingStatus(BaseActivity.this::handle));
        utils.clearCache();
        kujonApplication.finishAllAcitities();
        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void setToolbarTitle(int title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(googleToLogout){
            performGoogleLogout();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    protected void cancelCall(Call call) {
        if (call != null) call.cancel();
    }


    protected Call backendCall;


    protected void cancelLastCallIfExist() {
        if(backendCall != null){
            cancelCall(backendCall);
        }
    }
}
