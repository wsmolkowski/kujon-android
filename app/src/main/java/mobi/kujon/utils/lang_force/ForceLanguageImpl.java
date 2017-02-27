package mobi.kujon.utils.lang_force;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import mobi.kujon.R;
import mobi.kujon.utils.shared_preferences.SharedPreferencesFacade;

/**
 *
 */

public class ForceLanguageImpl implements ForceLanguage {
    public static final String IS_FORCED = "IS_FORCED";
    public static final String LAST_LANGUAGE = "LAST_LANGUAGE";
    public static String LOCAL_POLISH = "pl";
    public static String LOCAL_ENG = "en";


    private Context context;
    private SharedPreferencesFacade sharedPreferencesFacade;

    public ForceLanguageImpl(Context context, SharedPreferencesFacade sharedPreferencesFacade) {
        this.context = context;
        this.sharedPreferencesFacade = sharedPreferencesFacade;
    }

    @Override
    public boolean isLanguageForced() {
        return sharedPreferencesFacade.retrieveBoolean(IS_FORCED);
    }

    @Override
    public int getText() {
        if (isLocalPolish()) {
            return R.string.ang_enable;
        } else {
            return R.string.polish_enable;
        }
    }

    @Override
    public void setForced(boolean forced) {
        this.sharedPreferencesFacade.putBoolean(IS_FORCED, forced);
        if(forced){
            this.sharedPreferencesFacade.putString(LAST_LANGUAGE,Locale.getDefault().getLanguage());
        }else {

            Locale newLocale = new Locale(lastLocale());
            Locale.setDefault(newLocale);
            Configuration config = new Configuration();

            config.locale = newLocale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }

    private String lastLocale() {
        return sharedPreferencesFacade.retriveString(LAST_LANGUAGE, Locale.getDefault().getLanguage());
    }

    @Override
    public void setLocale(Activity context) {
        if (sharedPreferencesFacade.retrieveBoolean(IS_FORCED)) {
            Locale locale;
            if (isLocalPolish()) {
                locale = new Locale(LOCAL_ENG);
            } else {
                locale = new Locale(LOCAL_POLISH);
            }
            Locale.setDefault(locale);
            Configuration config = new Configuration();

            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        }
    }


    private boolean isLocalPolish() {
        return lastLocale().contains(LOCAL_POLISH);
    }
}
