package mobi.kujon.google_drive.model.json;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static mobi.kujon.google_drive.model.json.ShareFileTargetType.ALL;
import static mobi.kujon.google_drive.model.json.ShareFileTargetType.LIST;
import static mobi.kujon.google_drive.model.json.ShareFileTargetType.NONE;


@StringDef({ALL, NONE, LIST})
@Retention(RetentionPolicy.SOURCE)
public @interface ShareFileTargetType {
    String ALL = "All";
    String NONE = "None";
    String LIST = "List";
}

