package mobi.kujon.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;

import javax.inject.Inject;

import mobi.kujon.KujonApplication;
import mobi.kujon.network.json.Config;
import mobi.kujon.network.json.KujonResponse;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Response;

public class KujonUtils {

    private static final String TAG = "KujonUtils";

    @Inject Cache cache;
    @Inject OkHttpClient httpClient;

    public KujonUtils() {
        KujonApplication.getComponent().inject(this);
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) KujonApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void clearCache() {
        try {
            cache.evictAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void invalidateEntry(String urlToInvalidate) {
        Log.d(TAG, "invalidateEntry() called with: " + "urlToInvalidate = [" + urlToInvalidate + "]");
        try {
            Cache cache = httpClient.cache();
            Iterator<String> urls = cache.urls();
            while (urls.hasNext()) {
                String url = urls.next();
                if (url.contains(urlToInvalidate)) {
                    urls.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> Integer getResponseCode(Response<KujonResponse<T>> response) {
        try {
            return response.body().code;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
