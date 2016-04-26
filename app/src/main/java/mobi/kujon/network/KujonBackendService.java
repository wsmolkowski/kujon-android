package mobi.kujon.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import mobi.kujon.KujonApplication;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KujonBackendService {

    private static final String TAG = "KujonBackendService";

    private final OkHttpClient httpClient;
    private KujonBackendApi kujonBackendApi;
    private static KujonBackendService instance;

    private Context context = KujonApplication.getApplication();
    private final File httpCacheDirectory;

    private KujonBackendService() {

        httpCacheDirectory = new File(context.getCacheDir(),  "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .addNetworkInterceptor(new AuthenticationInterceptor())
//                .addInterceptor(logging)
                .addInterceptor(OFFLINE_INTERCEPTOR)
                .cache(cache)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kujon.mobi")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        kujonBackendApi = retrofit.create(KujonBackendApi.class);
    }

    public KujonBackendApi getKujonBackendApi() {
        return kujonBackendApi;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public synchronized static KujonBackendService getInstance() {
        if (instance == null) {
            instance = new KujonBackendService();
        }

        return instance;
    }

    private static class AuthenticationInterceptor implements Interceptor {

        @Override public Response intercept(Chain chain) throws IOException {
            Log.i(TAG, "AuthenticationInterceptor");

            GoogleSignInResult loginStatus = KujonApplication.getApplication().getLoginStatus();

            Request originalRequest = chain.request();
            Log.i(TAG, "AuthenticationInterceptor: modifying request" + originalRequest.url());
            GoogleSignInAccount account = loginStatus.getSignInAccount();

            String email = account != null ? account.getEmail() : "";
            String token = account != null ? account.getIdToken() : "";

            Request request = originalRequest.newBuilder()
//                    .header("Cookie", "KUJON_SECURE_COOKIE=\"2|1:0|10:1461062516|19:KUJON_SECURE_COOKIE|292:IntcImFjY2Vzc190b2tlbl9rZXlcIjogXCJ6R3paYVdOajMySG5zWU1QNjlyYlwiLCBcIl9pZFwiOiB7XCIkb2lkXCI6IFwiNTcxNjBhZTc4NTVlZjkxNzIxN2IxMjk2XCJ9LCBcImFjY2Vzc190b2tlbl9zZWNyZXRcIjogXCJSYnFMdWRLVFVWM1NkZEtiWjJxcXZoR0VZVHJ3QnBVVUtHY2pyMzlXXCIsIFwidXNvc19pZFwiOiBcIkRFTU9cIiwgXCJ1c29zX3BhaXJlZFwiOiB0cnVlfSI=|7200d93cb11ec3a3c63ce13378ffc37cf21b4f6b8286f4a87a097212dc391ac1\"")
                    .header("X-Kujonmobiemail", email)
                    .header("X-Kujonmobitoken", token)
                    .build();

//            long t1 = System.nanoTime();
//            System.out.println(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

//            long t2 = System.nanoTime();
//            System.out.println(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }

    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
        Log.i(TAG, "REWRITE_RESPONSE_INTERCEPTOR");

        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 5)
                    .build();
        } else {
            return originalResponse;
        }

    };

    private static final Interceptor OFFLINE_INTERCEPTOR = chain -> {
        boolean online = isOnline();
        Log.i(TAG, "OFFLINE_INTERCEPTOR online=" + online);

        Request request = chain.request();

        if (!online) {
            Log.d(TAG, "rewriting request");

            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        return chain.proceed(request);
    };

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) KujonApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
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

    public File getHttpCacheDirectory() {
        return httpCacheDirectory;
    }
}
