package mobi.kujon;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.utils.KujonUtils;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    private static final String TAG = "NetModule";

    @Provides @Singleton Cache provideOkHttpCache(Application application) {
        File httpCacheDirectory = new File(application.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(httpCacheDirectory, cacheSize);
    }

    @Provides @Singleton OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .addNetworkInterceptor(new AuthenticationInterceptor())
                .addInterceptor(OFFLINE_INTERCEPTOR)
                .cache(cache)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }

    @Provides @Singleton Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kujon.mobi")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Provides @Singleton KujonBackendApi provideBackendApi(Retrofit retrofit) {
        return retrofit.create(KujonBackendApi.class);
    }

    @Provides @Singleton KujonUtils provideUtils() {
        return new KujonUtils();
    }

    @Provides @Singleton Picasso providePicasso(Application application, OkHttpClient httpClient) {
        Picasso picasso = new Picasso.Builder(application)
                .downloader(new OkHttp3Downloader(httpClient))
                .build();

        picasso.setIndicatorsEnabled(BuildConfig.DEBUG);
        picasso.setLoggingEnabled(BuildConfig.DEBUG);
        return picasso;
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
                    .removeHeader("If-None-Match")
                    .build();

            return chain.proceed(request);
        }
    }

    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
        Log.i(TAG, "REWRITE_RESPONSE_INTERCEPTOR");

        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");

        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 30)
                    .build();
        } else {
            return originalResponse;
        }

    };

    private static final Interceptor OFFLINE_INTERCEPTOR = chain -> {
        boolean online = KujonUtils.isOnline();
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
}


