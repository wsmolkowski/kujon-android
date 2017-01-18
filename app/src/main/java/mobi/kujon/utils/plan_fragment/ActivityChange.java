package mobi.kujon.utils.plan_fragment;

import android.support.annotation.StringRes;

/**
 *
 */

public interface ActivityChange {

    void dataDowloaded();
    void startLoading();
    void stopLoading();
    void showToast(@StringRes int id, String text);
}
