package mobi.kujon.google_drive.mvp.files_list;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static mobi.kujon.google_drive.mvp.files_list.FilesType.ALL;
import static mobi.kujon.google_drive.mvp.files_list.FilesType.MY;

/**
 *
 */

@IntDef({ALL,MY})
@Retention(RetentionPolicy.SOURCE)
public @interface FilesType {
    int ALL = 1;
    int MY =0;
}
