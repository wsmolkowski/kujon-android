package mobi.kujon;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import mobi.kujon.network.KujonBackendService;

public class KujonApplication extends Application implements OneSignal.NotificationOpenedHandler {

    private static final String TAG = "KujonApplication";

    private List<Activity> stack = new ArrayList<>();

    private static KujonApplication instance;
    private GoogleSignInResult loginStatus;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        Fabric.with(this, new Crashlytics());
//         Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(this)
                .init();

        OneSignal.enableNotificationsWhenActive(true);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                stack.add(activity);
            }

            @Override public void onActivityStarted(Activity activity) {
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
                stack.remove(activity);
            }
        });
    }

    public static KujonApplication getApplication() {
        return instance;
    }

    public GoogleSignInResult getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(GoogleSignInResult loginStatus) {
        this.loginStatus = loginStatus;
    }

    @Override public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
        KujonBackendService.getInstance().invalidateEntry("grades");
    }
}
