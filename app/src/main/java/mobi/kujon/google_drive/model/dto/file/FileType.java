package mobi.kujon.google_drive.model.dto.file;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static mobi.kujon.google_drive.model.dto.file.FileType.DEFAULT;

/**
 *
 */
@StringDef(DEFAULT)
@Retention(RetentionPolicy.SOURCE)
public @interface FileType {
    String DEFAULT = "default";
}
