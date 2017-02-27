package mobi.kujon.google_drive.model.dto.file_stream;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */

@IntDef({FileStreamType.FILE_UPLOADED_TO_KUJON,FileStreamType.FILE_DELETED_FROM_KUJON,FileStreamType.OTHER})
@Retention(RetentionPolicy.SOURCE)
public @interface FileStreamType {
    int FILE_UPLOADED_TO_KUJON  = 0;
    int FILE_DELETED_FROM_KUJON = 1;
    int OTHER = 2;
}
