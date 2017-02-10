package mobi.kujon.google_drive.ui.dialogs.sort_strategy;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */


@IntDef({SortStrategyType.DATE, SortStrategyType.FILE_NAME, SortStrategyType.AUTHOR})
@Retention(RetentionPolicy.SOURCE)
public @interface SortStrategyType {
    int DATE = 0;
    int FILE_NAME = 1;
    int AUTHOR = 2;
}
