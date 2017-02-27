package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.BuildConfig;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Preferences;
import mobi.kujon.utils.ErrorHandlerUtil;
import mobi.kujon.utils.shared_preferences.SharedPreferencesFacade;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesActivity extends BaseActivity {

    private static final String NOTIFICATION_PREFS_KEY = "NOTIFICATION_PREFS_KEY";
    private static final String CALENDAR_PREFS_KEY = "CALENDAR_PREFS_KEY";
    @Inject
    SharedPreferencesFacade sharedPreferencesFacade;



    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.notifications_enabler)
    SwitchCompat notificationsSwitch;
    @Bind(R.id.googlecalendar_enabler)
    SwitchCompat googleCalendarSwitch;
    @Bind(R.id.polish_enable)
    SwitchCompat languageSwitch;
    @Bind(R.id.language_text)
    TextView languageText;

    @Bind(R.id.app_version_text)
    TextView versionText;
    private boolean notificationSwitchChangeCalledByUser = true;
    private boolean googleCalendarSwitchChangeCalledByUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_settings);
        KujonApplication.getComponent().inject(this);
        ButterKnife.bind(this);
        utils.invalidateEntry("settings");

        showProgress(true);
        toolbarTitle.setText(R.string.settings);
        initSwitches();
        versionText.setText(getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME);
        languageText.setText(forceLanguage.getText());
        languageSwitch.setChecked(forceLanguage.isLanguageForced());
        languageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            forceLanguage.setForced(isChecked);
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }

    private void initSwitches() {
        if (sharedPreferencesFacade.contaisKey(NOTIFICATION_PREFS_KEY)) {
            notificationsSwitch.setChecked(sharedPreferencesFacade.retrieveBoolean(NOTIFICATION_PREFS_KEY));
        }
        if (sharedPreferencesFacade.contaisKey(CALENDAR_PREFS_KEY)) {
            googleCalendarSwitch.setChecked(sharedPreferencesFacade.retrieveBoolean(CALENDAR_PREFS_KEY));
        }
        kujonBackendApi.getUserPreferences().enqueue(new Callback<KujonResponse<Preferences>>() {
            @Override
            public void onResponse(Call<KujonResponse<Preferences>> call, Response<KujonResponse<Preferences>> response) {
                showProgress(false);
                if (ErrorHandlerUtil.handleResponse(response)) {
                    notificationsSwitch.setChecked(response.body().data.notificationsEnabled);
                    sharedPreferencesFacade.putBoolean(NOTIFICATION_PREFS_KEY, response.body().data.notificationsEnabled);
                    googleCalendarSwitch.setChecked(response.body().data.googleCalendarEnabled);
                    sharedPreferencesFacade.putBoolean(CALENDAR_PREFS_KEY, response.body().data.googleCalendarEnabled);
                    initSwitchListeners();
                }

                handler.postDelayed(() -> {
                    showProgress(false);
                }, 200);
            }

            @Override
            public void onFailure(Call<KujonResponse<Preferences>> call, Throwable t) {
                showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private void initSwitchListeners() {
        initNotificationsSwitchListener();
        initGoogleCalendarSwitchListener();
    }

    private void initGoogleCalendarSwitchListener() {
        googleCalendarSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (googleCalendarSwitchChangeCalledByUser) {
                showProgress(true);
                setGoogleCalendar(isChecked);
            }
        });
    }

    private void setGoogleCalendar(boolean isChecked) {
        settingsApi.setGoogleCalendar(isChecked).enqueue(new Callback<KujonResponse<String>>() {
            @Override
            public void onResponse(Call<KujonResponse<String>> call, Response<KujonResponse<String>> response) {
                showProgress(false);
                if (!ErrorHandlerUtil.handleResponse(response)) {
                    programGoogleCalendarSwitchChange(isChecked);
                }else {
                    sharedPreferencesFacade.putBoolean(CALENDAR_PREFS_KEY, isChecked);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<String>> call, Throwable t) {
                showProgress(false);
                programGoogleCalendarSwitchChange(isChecked);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private void programGoogleCalendarSwitchChange(boolean isChecked) {
        googleCalendarSwitchChangeCalledByUser = false;
        googleCalendarSwitch.setChecked(!isChecked);
        googleCalendarSwitchChangeCalledByUser = true;
    }

    private void initNotificationsSwitchListener() {
        notificationsSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (notificationSwitchChangeCalledByUser) {
                showProgress(true);
                setEvents(isChecked);
            }
        });
    }

    private void setEvents(boolean isChecked) {
        settingsApi.setEvents(isChecked).enqueue(new Callback<KujonResponse<String>>() {
            @Override
            public void onResponse(Call<KujonResponse<String>> call, Response<KujonResponse<String>> response) {
                showProgress(false);
                if (!ErrorHandlerUtil.handleResponse(response)) {
                    programNotificationSwitchChange(isChecked);
                }else {
                    sharedPreferencesFacade.putBoolean(NOTIFICATION_PREFS_KEY, isChecked);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<String>> call, Throwable t) {
                showProgress(false);
                programNotificationSwitchChange(isChecked);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private void programNotificationSwitchChange(boolean isChecked) {
        notificationSwitchChangeCalledByUser = false;
        notificationsSwitch.setChecked(!isChecked);
        notificationSwitchChangeCalledByUser = true;
    }

    @OnClick({R.id.logout, R.id.delete_account, R.id.regulations, R.id.contact_us, R.id.share_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.delete_account:
                deleteAccount();
                break;
            case R.id.contact_us:
                contactUs();
                break;
            case R.id.share_app:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_link));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.regulations:
                String url = getString(R.string.regulations_url);
                WebViewAcitivty.showUrl(this, url);
                break;
        }
    }
}