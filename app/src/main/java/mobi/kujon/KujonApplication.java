package mobi.kujon;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class KujonApplication extends Application {

    private static Application instance;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        Fabric.with(this, new Crashlytics());
//         Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
    }

    public static Application getContext() {
        return instance;
    }
}
