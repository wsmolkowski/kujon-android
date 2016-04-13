package mobi.kujon.network;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.io.IOException;

import mobi.kujon.KujonApplication;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KujonBackendService {

    private final OkHttpClient httpClient;
    private KujonBackendApi kujonBackendApi;
    private static KujonBackendService instance;

    private KujonBackendService() {
        httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new AuthenticationInterceptor())
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

    public synchronized static KujonBackendService getInstance() {
        if (instance == null) {
            instance = new KujonBackendService();
        }

        return instance;
    }

    private static class AuthenticationInterceptor implements Interceptor {

        @Override public Response intercept(Chain chain) throws IOException {

            GoogleSignInResult loginStatus = KujonApplication.getApplication().getLoginStatus();

            Request originalRequest = chain.request();
            System.out.println("### modifying request" + originalRequest.url());
            Request request = originalRequest.newBuilder()
                    .header("Cookie", "KUJON_SECURE_COOKIE=\"2|1:0|10:1460538517|19:KUJON_SECURE_COOKIE|292:IntcImFjY2Vzc190b2tlbl9rZXlcIjogXCJ6R3paYVdOajMySG5zWU1QNjlyYlwiLCBcIl9pZFwiOiB7XCIkb2lkXCI6IFwiNTcwZDNlNWI4NTVlZjkxZWE4M2NjMjE5XCJ9LCBcImFjY2Vzc190b2tlbl9zZWNyZXRcIjogXCJSYnFMdWRLVFVWM1NkZEtiWjJxcXZoR0VZVHJ3QnBVVUtHY2pyMzlXXCIsIFwidXNvc19wYWlyZWRcIjogdHJ1ZSwgXCJ1c29zX2lkXCI6IFwiREVNT1wifSI=|f3eb6d3bc96195e7d2e263fccc7a5c38b79f60463adf80d6b5b582320d79714f\"")
                    .header("X-kujon.mobi-email", loginStatus.getSignInAccount().getEmail())
                    .header("X-kujon.mobi-token", loginStatus.getSignInAccount().getIdToken())
                    .build();

            long t1 = System.nanoTime();
            System.out.println(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            System.out.println(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
