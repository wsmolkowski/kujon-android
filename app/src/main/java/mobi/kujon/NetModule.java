package mobi.kujon;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import bolts.Task;
import dagger.Module;
import dagger.Provides;
import mobi.kujon.network.ApiProvider;
import mobi.kujon.network.KujonBackendApi;
import mobi.kujon.network.SettingsApi;
import mobi.kujon.network.json.GradeClassType;
import mobi.kujon.utils.KujonUtils;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class NetModule {

    private static final String TAG = "NetModule";

    @Provides @Singleton Cache provideOkHttpCache(Application application) {
        File httpCacheDirectory = new File(application.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(httpCacheDirectory, cacheSize);
    }

    @Provides @Singleton OkHttpClient provideOkHttpClient(Cache cache,AuthenticationInterceptor authenticationInterceptor) {
        HttpLoggingInterceptor.Logger myLogger = message -> Log.d("CustomLogRetrofit", message);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(myLogger);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        CookieManager cookieManager = new CookieManager();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .addNetworkInterceptor(loggingInterceptor)
                .addNetworkInterceptor(authenticationInterceptor)
                .addInterceptor(OFFLINE_INTERCEPTOR)
                .cache(cache)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }


    @Provides
    @Singleton
    AuthenticationInterceptor providesAuthenticationInterceptor(){
        return new AuthenticationInterceptor();
    }
    @Provides @Singleton Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(GradeClassType.class, new GradeClassType.GradeClassTypeDeserializer())
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    @Provides @Singleton
    ApiProvider provideApiProvider(OkHttpClient okHttpClient, Gson gson) {
        return new ApiProvider(okHttpClient, gson);
    }


    @Provides KujonBackendApi provideBackendApi(ApiProvider apiProvider) {
        return apiProvider.getKujonBackendApi();
    }

    @Provides @Singleton
    SettingsApi provideSettingsApi(KujonBackendApi kujonBackendApi) {
        return new SettingsApi(kujonBackendApi);
    }

    @Provides @Singleton KujonUtils provideUtils() {
        return new KujonUtils();
    }

    @Provides @Singleton Picasso providePicasso(Application application, OkHttpClient httpClient) {
        return new Picasso.Builder(application)
                .downloader(new OkHttp3Downloader(httpClient))
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofitProvider(ApiProvider apiProvider){
        return apiProvider.provideRetrofit();
    }

     public static class AuthenticationInterceptor implements Interceptor {

        @Override public Response intercept(Chain chain) throws IOException {
            Log.i(TAG, "AuthenticationInterceptor");

            Task<GoogleSignInResult> loginStatus = KujonApplication.getApplication().getLoginStatus();

            Request originalRequest = chain.request();
            String email = "";
            String token = "";
            if (loginStatus.isCompleted() && loginStatus.getResult() != null && loginStatus.getResult().getSignInAccount() != null) {
                GoogleSignInAccount account = loginStatus.getResult().getSignInAccount();
                email = account.getEmail();
                token = account.getIdToken();
            }

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


