package mobi.kujon.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KujonBackendService {

    private KujonBackendApi kujonBackendApi;
    private static KujonBackendService instance;

    private KujonBackendService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kujon.mobi")
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
}
