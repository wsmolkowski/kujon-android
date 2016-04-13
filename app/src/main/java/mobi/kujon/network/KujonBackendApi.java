package mobi.kujon.network;

import java.util.List;

import mobi.kujon.network.json.Config;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Usos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface KujonBackendApi {

    @Headers({
            "Accept: application/json",
//            "Cookie: KUJON_SECURE_COOKIE=\"2|1:0|10:1460457548|19:KUJON_SECURE_COOKIE|104:IntcIl9pZFwiOiB7XCIkb2lkXCI6IFwiNTcwY2QwNGM4NTVlZjkyZTE2OGJhN2QwXCJ9LCBcInVzb3NfcGFpcmVkXCI6IGZhbHNlfSI=|f20f97fb9898caa953acd4641d9646592f35a5e7d1e728de587a06b47b0307b1\""
    })
    @GET("usoses") Call<KujonResponse<List<Usos>>> usoses();

    @GET("config") Call<KujonResponse<Config>> config();

    @Headers({
            "Accept: application/json",
//            "Cookie: KUJON_SECURE_COOKIE=\"2|1:0|10:1460457548|19:KUJON_SECURE_COOKIE|104:IntcIl9pZFwiOiB7XCIkb2lkXCI6IFwiNTcwY2QwNGM4NTVlZjkyZTE2OGJhN2QwXCJ9LCBcInVzb3NfcGFpcmVkXCI6IGZhbHNlfSI=|f20f97fb9898caa953acd4641d9646592f35a5e7d1e728de587a06b47b0307b1\""
    })
    @POST("authentication/register") Call<KujonResponse> register();
}
