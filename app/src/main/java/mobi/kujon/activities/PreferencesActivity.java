package mobi.kujon.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import mobi.kujon.R;

public class PreferencesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.settings);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        private BaseActivity activity;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            activity = (BaseActivity) getActivity();

            Preference logout = findPreference("logout");
            logout.setOnPreferenceClickListener(preference -> {
                activity.logout();
                return true;
            });

            Preference deleteAccount = findPreference("delete_account");
            deleteAccount.setOnPreferenceClickListener(preference -> {
                activity.deleteAccount();
                return true;
            });

            Preference regulations = findPreference("regulations");
            regulations.setOnPreferenceClickListener(preference -> {
                String url = activity.getString(R.string.regulations_url);
                WebViewAcitivty.showUrl(activity, url);
                return true;
            });
        }
    }

}