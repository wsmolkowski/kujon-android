package mobi.kujon.google_drive.network.api;

/**
 *
 */

public class ApiConst {
    static final String CACHE_CONTROL = "Cache-Control";
    static final String X_KUJONREFRESH = "X-Kujonrefresh";


    public static String getCacheValue(boolean b){
       return b?"no-cache":null;
    }
}
