package mobi.kujon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mobi.kujon.R;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Preferences;
import mobi.kujon.utils.ErrorHandlerUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesActivity extends BaseActivity {

    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.notifications_enabler) SwitchCompat notificationsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_settings);
        ButterKnife.bind(this);
        showProgress(true);
        toolbarTitle.setText(R.string.settings);
        initSwitchStates();
        initSwitchListeners();
    }

    private void initSwitchStates() {
        kujonBackendApi.getUserPreferences().enqueue(new Callback<KujonResponse<Preferences>>() {
            @Override
            public void onResponse(Call<KujonResponse<Preferences>> call, Response<KujonResponse<Preferences>> response) {
                showProgress(false);
                if(ErrorHandlerUtil.handleResponse(response)) {
                    notificationsSwitch.setChecked(response.body().data.notificationsEnabled);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<Preferences>> call, Throwable t) {
                showProgress(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private void initSwitchListeners() {
        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                showProgress(true);
                if (isChecked) {
                    enableEvents();
                } else {
                    disableEvents();
                }
            }
        });
    }

    private void enableEvents() {
        kujonBackendApi.enableEvents().enqueue(new Callback<KujonResponse<Preferences>>() {
            @Override
            public void onResponse(Call<KujonResponse<Preferences>> call, Response<KujonResponse<Preferences>> response) {
                showProgress(false);
                if(ErrorHandlerUtil.handleResponse(response)) {
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<Preferences>> call, Throwable t) {
                showProgress(false);
                notificationsSwitch.setChecked(false);
                ErrorHandlerUtil.handleError(t);
            }
        });
    }

    private void disableEvents() {
        showProgress(true);
        kujonBackendApi.disableEvents().enqueue(new Callback<KujonResponse<Preferences>>() {
            @Override
            public void onResponse(Call<KujonResponse<Preferences>> call, Response<KujonResponse<Preferences>> response) {
                showProgress(false);
                if(ErrorHandlerUtil.handleResponse(response)) {
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<KujonResponse<Preferences>> call, Throwable t) {
                showProgress(false);
                notificationsSwitch.setChecked(true);
                ErrorHandlerUtil.handleError(t);
            }
        });
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