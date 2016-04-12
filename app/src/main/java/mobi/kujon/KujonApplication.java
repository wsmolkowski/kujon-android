package mobi.kujon;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import mobi.kujon.activities.BaseActivity;
import mobi.kujon.activities.LoginActivity;

public class KujonApplication extends Application implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "KujonApplication";

    private List<Activity> stack = new ArrayList<>();

    private static KujonApplication instance;
    private GoogleApiClient apiClient;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        Fabric.with(this, new Crashlytics());
//         Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.serverClientId))
                .requestProfile()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                stack.add(activity);
            }

            @Override public void onActivityStarted(Activity activity) {
                if (activity instanceof BaseActivity) {
                    checkLoggingStatus(KujonApplication.this::handle);
                }
            }

            @Override public void onActivityResumed(Activity activity) {
            }

            @Override public void onActivityPaused(Activity activity) {
            }

            @Override public void onActivityStopped(Activity activity) {
            }

            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public void checkLoggingStatus(ResultCallback<GoogleSignInResult> resultCallback) {
        OptionalPendingResult<GoogleSignInResult> silentSignIn = Auth.GoogleSignInApi.silentSignIn(apiClient);
        if (silentSignIn.isDone()) {
            resultCallback.onResult(silentSignIn.get());
        } else {
            silentSignIn.setResultCallback(resultCallback);
        }
    }

    public void handle(GoogleSignInResult result) {
        boolean hasLoginActivity = stack.size() > 0 && stack.get(0) instanceof LoginActivity;
        boolean userIsLoggedIn = result.isSuccess();

        if (!hasLoginActivity && !userIsLoggedIn) {
            Log.i(TAG, "!hasLoginActivity && !userIsLoggedIn");
            for (Activity activity : stack) {
                activity.finish();
            }
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
    }

    public void logout() {
        Log.d(TAG, "logout() called with: " + "");
        Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(status -> checkLoggingStatus(this::handle));
    }

    public static KujonApplication getApplication() {
        return instance;
    }

    public GoogleApiClient getApiClient() {
        return apiClient;
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }
}
