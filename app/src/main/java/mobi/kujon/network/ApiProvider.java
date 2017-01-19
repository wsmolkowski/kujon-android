package mobi.kujon.network;


import com.google.gson.Gson;

import mobi.kujon.BuildConfig;
import mobi.kujon.google_drive.KujonFilesharingApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider implements ApiChoice {


    private
    @ApiType.ApiTypes
    int currentApiType;
    private OkHttpClient client;
    private Gson gson;
    private KujonBackendApi kujonBackendApi;
    private KujonFilesharingApi filesharingApi;

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
        createKujonFileSharpingApi();
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
        kujonBackendApi = new Retrofit.Builder()
                .baseUrl(getApiURL())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(KujonBackendApi.class);
    }

    private void createKujonFileSharpingApi() {
        filesharingApi = new Retrofit.Builder()
                .baseUrl(getApiURL())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(KujonFilesharingApi.class);
    }

    public KujonFilesharingApi getKujonFilesharingApi() {
        return filesharingApi;
    }
}
