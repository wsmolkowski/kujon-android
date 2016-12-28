package mobi.kujon.network;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ApiType {
    public static final int DEMO = 0;
    public static final int PROD = 1;

    @IntDef({DEMO, PROD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ApiTypes {}
}
