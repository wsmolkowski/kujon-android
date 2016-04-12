package mobi.kujon;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class KujonApplication extends Application {

    private static final String TAG = "KujonApplication";

    private List<Activity> stack = new ArrayList<>();

    private static KujonApplication instance;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        Fabric.with(this, new Crashlytics());
//         Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

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
}
