package mobi.kujon.utils.lang_force;

import android.app.Activity;
import android.support.annotation.StringRes;

/**
 *
 */

public interface ForceLanguage {
    boolean isLanguageForced();
    @StringRes int getText();
    void setForced(boolean forced);
    void setLocale(Activity context);
}
