package mobi.kujon.network;


import com.google.gson.Gson;

import mobi.kujon.BuildConfig;
import mobi.kujon.google_drive.network.KujonFilesharingApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider implements ApiChoice,ProvideRetrofit {


    private
    @ApiType.ApiTypes
    int currentApiType;
    private OkHttpClient client;
    private Gson gson;
    private KujonBackendApi kujonBackendApi;
    private Retrofit retrofit;

    public ApiProvider(OkHttpClient okHttpClient, Gson gson) {
        this.client = okHttpClient;
        this.gson = gson;
        if(BuildConfig.DEBUG){
            setApiType(ApiType.DEMO);
        }else {
            setApiType(ApiType.PROD);
        }
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
    public @ApiType.ApiTypes int getApiType() {
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
        assignRetrofit();
        kujonBackendApi = retrofit.create(KujonBackendApi.class);
    }

    private void assignRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(getApiURL())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Override
    public Retrofit provideRetrofit() {
        return retrofit;
    }
}
