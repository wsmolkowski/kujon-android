package mobi.kujon;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bolts.Task;
import bolts.TaskCompletionSource;
import io.fabric.sdk.android.Fabric;
import mobi.kujon.activities.MainActivity;
import mobi.kujon.google_drive.dagger.DaggerRuntimeFilesComponent;
import mobi.kujon.google_drive.dagger.FilesApiFacadesModule;
import mobi.kujon.google_drive.dagger.FilesComponent;
import mobi.kujon.google_drive.dagger.FilesModule;
import mobi.kujon.google_drive.dagger.injectors.InjectorProvider;
import mobi.kujon.utils.KujonUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class KujonApplication extends Application implements OneSignal.NotificationOpenedHandler {

    public static final String USER_EMAIL_TAG = "user_email";
    public static final String FROM_NOTIFICATION = "FROM_NOTIFICATION";



    @Inject KujonUtils utils;
    public InjectorProvider injectorProvider;

    public static String DEVICE_ID;

    private List<Activity> stack = new ArrayList<>();

    private static KujonApplication instance;
    private static KujonComponent component;
    private FilesComponent filesComponent;

    private Activity topActivity;
    private TaskCompletionSource<GoogleSignInResult> googleSignInResultTCS = new TaskCompletionSource<>();



    @Override public void onCreate() {
        super.onCreate();
        instance = this;

        DEVICE_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Fabric.with(this, new Crashlytics(), new Answers());
//         Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(this)
                .init();

        OneSignal.enableNotificationsWhenActive(true);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

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




        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public void finishAllAcitities() {
        for (Activity activity : stack) {
            activity.finish();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static KujonApplication getApplication() {
        return instance;
    }

    public Task<GoogleSignInResult> getLoginStatus() {
        return googleSignInResultTCS.getTask();
    }

    public void setLoginStatus(GoogleSignInResult loginStatus) {
        googleSignInResultTCS = new TaskCompletionSource<>();
        googleSignInResultTCS.setResult(loginStatus);
        if (loginStatus != null && loginStatus.getSignInAccount() != null) {
            OneSignal.sendTag(USER_EMAIL_TAG, loginStatus.getSignInAccount().getEmail());
        }
    }

    @Override public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(FROM_NOTIFICATION, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    public static KujonComponent getComponent() {
        return component;
    }

    public Activity getTopActivity() {
        return topActivity;
    }

    public FilesComponent getFilesComponent() {
        if(filesComponent ==null){
            buildFilesComponent();
        }
        return filesComponent;
    }
    public void resetFilesComponent(){
        filesComponent = null;
    }

    private void buildFilesComponent() {
        filesComponent = DaggerRuntimeFilesComponent.builder()
                .kujonComponent(component)
                .filesModule(new FilesModule(this))
                .filesApiFacadesModule(new FilesApiFacadesModule())
                .build();
    }

    public InjectorProvider getInjectorProvider() {
        if(filesComponent ==null){
            buildFilesComponent();
        }
        return injectorProvider;
    }
}
