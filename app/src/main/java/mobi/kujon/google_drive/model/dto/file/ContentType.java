package mobi.kujon.google_drive.model.dto.file;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static mobi.kujon.google_drive.model.dto.file.ContentType.IMAGE;

/**
 *
 */

@StringDef({IMAGE})
@Retention(RetentionPolicy.SOURCE)
public @interface ContentType {
    String IMAGE = "image/png";
}
