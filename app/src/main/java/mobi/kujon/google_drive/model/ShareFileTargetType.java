package mobi.kujon.google_drive.model;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static mobi.kujon.google_drive.model.ShareFileTargetType.ALL;
import static mobi.kujon.google_drive.model.ShareFileTargetType.LIST;
import static mobi.kujon.google_drive.model.ShareFileTargetType.NONE;


@StringDef({ALL, NONE, LIST})
@Retention(RetentionPolicy.SOURCE)
public @interface ShareFileTargetType {
    String ALL = "All";
    String NONE = "None";
    String LIST = "List";
}

