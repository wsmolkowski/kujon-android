package mobi.kujon;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.onesignal.OneSignal;
import com.percolate.foam.FoamApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import mobi.kujon.utils.KujonUtils;

//@FoamApiKeys(
//        papertrail = "logs3.papertrailapp.com:22247" // Server URL
//)
public class KujonApplication extends FoamApplication implements OneSignal.NotificationOpenedHandler {

    public static final String USER_EMAIL_TAG = "user_email";
    @Inject KujonUtils utils;

    private static final Logger log = LoggerFactory.getLogger(KujonApplication.class);
    public static String DEVICE_ID;

    private List<Activity> stack = new ArrayList<>();

    private static KujonApplication instance;
    private GoogleSignInResult loginStatus;
    private static KujonComponent component;

    private Activity topActivity;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;

        DEVICE_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        MDC.put("deviceId", DEVICE_ID);
        MDC.put("applicationId", BuildConfig.APPLICATION_ID);
        MDC.put("versionCode", "" + BuildConfig.VERSION_CODE);
        MDC.put("versionName", BuildConfig.VERSION_NAME);
        log.info("Application started");
        Fabric.with(this, new Crashlytics(), new Answers());
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
                topActivity = activity;
            }

            @Override public void onActivityPaused(Activity activity) {
                topActivity = null;
            }

            @Override public void onActivityStopped(Activity activity) {
            }

            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override public void onActivityDestroyed(Activity activity) {
                stack.remove(activity);
            }
        });

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                return super.placeholder(ctx, tag);
            }
        });

        component = DaggerKujonComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();

        component.inject(this);
    }

    public void finishAllAcitities(){
        for (Activity activity : stack) {
            activity.finish();
        }
    }

    public static KujonApplication getApplication() {
        return instance;
    }

    public GoogleSignInResult getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(GoogleSignInResult loginStatus) {
        this.loginStatus = loginStatus;
        if (loginStatus != null && loginStatus.getSignInAccount() != null) {
            OneSignal.sendTag(USER_EMAIL_TAG, loginStatus.getSignInAccount().getEmail());
        }
    }

    @Override public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
        utils.invalidateEntry("grades");
    }

    public static KujonComponent getComponent() {
        return component;
    }

    public Activity getTopActivity() {
        return topActivity;
    }
}
