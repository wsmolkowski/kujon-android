package mobi.kujon.network;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

            GoogleSignInResult loginStatus = KujonApplication.getApplication().getLoginStatus();

            Request originalRequest = chain.request();
            System.out.println("### modifying request" + originalRequest.url());
            GoogleSignInAccount account = loginStatus.getSignInAccount();

            String email = account != null ? account.getEmail() : "";
            String token = account != null ? account.getIdToken() : "";

            Request request = originalRequest.newBuilder()
                    .header("Cookie", "KUJON_SECURE_COOKIE=\"2|1:0|10:1461062516|19:KUJON_SECURE_COOKIE|292:IntcImFjY2Vzc190b2tlbl9rZXlcIjogXCJ6R3paYVdOajMySG5zWU1QNjlyYlwiLCBcIl9pZFwiOiB7XCIkb2lkXCI6IFwiNTcxNjBhZTc4NTVlZjkxNzIxN2IxMjk2XCJ9LCBcImFjY2Vzc190b2tlbl9zZWNyZXRcIjogXCJSYnFMdWRLVFVWM1NkZEtiWjJxcXZoR0VZVHJ3QnBVVUtHY2pyMzlXXCIsIFwidXNvc19pZFwiOiBcIkRFTU9cIiwgXCJ1c29zX3BhaXJlZFwiOiB0cnVlfSI=|7200d93cb11ec3a3c63ce13378ffc37cf21b4f6b8286f4a87a097212dc391ac1\"")
//                    .header("X-Kujonmobiemail", email)
//                    .header("X-Kujonmobitoken", token)
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
