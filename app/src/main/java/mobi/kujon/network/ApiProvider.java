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
    private Retrofit retrofit;
    private KujonBackendApi kujonBackendApi;

    public ApiProvider(OkHttpClient okHttpClient, Gson gson) {
        this.client = okHttpClient;
        this.gson = gson;
        setApiType(ApiType.PROD);
    }

    @Override
    public void setApiType(@ApiType.ApiTypes int apiType) {
        currentApiType = apiType;
        createRetrofit();
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

    private void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(getApiURL())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
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
        kujonBackendApi = retrofit.create(KujonBackendApi.class);
    }
}
