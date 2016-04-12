package mobi.kujon.network;

import mobi.kujon.network.json.UsosesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface KujonBackendService {

    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "Cookie: KUJON_SECURE_COOKIE=\"2|1:0|10:1460457548|19:KUJON_SECURE_COOKIE|104:IntcIl9pZFwiOiB7XCIkb2lkXCI6IFwiNTcwY2QwNGM4NTVlZjkyZTE2OGJhN2QwXCJ9LCBcInVzb3NfcGFpcmVkXCI6IGZhbHNlfSI=|f20f97fb9898caa953acd4641d9646592f35a5e7d1e728de587a06b47b0307b1\""
    })
    @GET("usoses") Call<UsosesResponse> usoses();
}
