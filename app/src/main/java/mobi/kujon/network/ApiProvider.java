package mobi.kujon.network;


import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider implements ApiChoice {


    private
    @ApiType.ApiTypes
    int currentApiType;
    private Map<Integer, String> urls = new HashMap<>();
    private Map<Integer, Retrofit> clients = new HashMap<>();
    private Map<Integer, KujonBackendApi> kujonBackendApis = new HashMap<>();
    private Map<Integer, SettingsApi> settingsApis = new HashMap<>();

    public ApiProvider(OkHttpClient okHttpClient, Gson gson) {
        this.currentApiType = ApiType.PROD;
        setURLs();
        setClients(okHttpClient, gson);
        setApis();
    }

    private void setURLs() {
        urls.put(ApiType.PROD, "https://api.kujon.mobi/");
        urls.put(ApiType.DEMO, "https://api-demo.kujon.mobi");
    }

    private void setClients(OkHttpClient okHttpClient, Gson gson) {
        for (Map.Entry<Integer, String> urlsEntry : urls.entrySet()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(urlsEntry.getValue())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            clients.put(urlsEntry.getKey(), retrofit);
        }
    }

    private void setApis() {
        for (Map.Entry<Integer, Retrofit> client : clients.entrySet()) {
            int apiType = client.getKey();
            kujonBackendApis.put(apiType, client.getValue().create(KujonBackendApi.class));
            settingsApis.put(apiType, new SettingsApi(kujonBackendApis.get(apiType)));
        }
    }

    public KujonBackendApi getKujonBackendApi() {
        return kujonBackendApis.get(currentApiType);
    }

    public SettingsApi getSettingsApi() {
        return settingsApis.get(currentApiType);
    }

    @Override
    public void setApiType(@ApiType.ApiTypes int apiType) {
        currentApiType = apiType;
    }

    @Override
    public int getApiType() {
        return currentApiType;
    }
}
