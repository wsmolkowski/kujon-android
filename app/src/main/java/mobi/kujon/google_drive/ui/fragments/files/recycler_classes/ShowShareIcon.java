package mobi.kujon.google_drive.ui.fragments.files.recycler_classes;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 *
 */
public interface ShowShareIcon {
    void showShareIcon(@DrawableRes int res, @StringRes int stringRes);
    void showShareIcon(@DrawableRes int res, String stringRes);
    void hide();
}
