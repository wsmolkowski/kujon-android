package mobi.kujon.network;


import com.google.gson.Gson;

import mobi.kujon.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider implements ApiChoice {


    private
    @ApiType.ApiTypes
    int currentApiType;
    private OkHttpClient client;
    private Gson gson;
    private KujonBackendApi kujonBackendApi;

    public ApiProvider(OkHttpClient okHttpClient, Gson gson) {
        this.client = okHttpClient;
        this.gson = gson;
        setApiType(ApiType.PROD);
    }

    @Override
    public void setApiType(@ApiType.ApiTypes int apiType) {
        currentApiType = apiType;
        createKujonBackendApi();
    }

    public void switchApiType() {
        switch (currentApiType) {
            case ApiType.DEMO:
                setApiType(ApiType.PROD);
                break;
            case ApiType.PROD:
                setApiType(ApiType.DEMO);
                break;
        }
    }

    @Override
    public int getApiType() {
        return currentApiType;
    }


    public String getApiURL() {
        switch (currentApiType) {
            case ApiType.DEMO:
                return BuildConfig.URL_DEMO;
            case ApiType.PROD:
                return BuildConfig.URL_PROD;
        }
        return BuildConfig.URL_PROD;
    }

    public KujonBackendApi getKujonBackendApi() {
        return kujonBackendApi;
    }

    private void createKujonBackendApi() {
        kujonBackendApi = new Retrofit.Builder()
                .baseUrl(getApiURL())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(KujonBackendApi.class);
    }
}
