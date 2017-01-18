package mobi.kujon.google_drive.model;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ShareFileTargetType {
    public static final String ALL = "All";
    public static final String NONE = "None";
    public static final String LIST = "List";

    @StringDef({ALL, NONE, LIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShareFileTargetTypes {}
}
