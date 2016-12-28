package mobi.kujon.network;


import com.google.gson.Gson;

import java.util.List;
import java.util.SortedMap;

import mobi.kujon.BuildConfig;
import mobi.kujon.network.json.CalendarEvent;
import mobi.kujon.network.json.Config;
import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.Grade;
import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Lecturer;
import mobi.kujon.network.json.LecturerLong;
import mobi.kujon.network.json.Message;
import mobi.kujon.network.json.Preferences;
import mobi.kujon.network.json.Programme;
import mobi.kujon.network.json.ProgrammeSingle;
import mobi.kujon.network.json.Term2;
import mobi.kujon.network.json.TermGrades;
import mobi.kujon.network.json.User;
import mobi.kujon.network.json.Usos;
import mobi.kujon.network.json.gen.CoursersSearchResult;
import mobi.kujon.network.json.gen.FacultiesSearchResult;
import mobi.kujon.network.json.gen.Faculty2;
import mobi.kujon.network.json.gen.ProgrammeSearchResult;
import mobi.kujon.network.json.gen.StudentSearchResult;
import mobi.kujon.network.json.gen.ThesesSearchResult;
import mobi.kujon.network.json.gen.Thesis_;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    private String getApiURL() {
        switch (currentApiType) {
            case ApiType.DEMO:
                return BuildConfig.URL_DEMO;
            case ApiType.PROD:
                return BuildConfig.URL_PROD;
        }
        return BuildConfig.URL_PROD;
    }

    public KujonBackendApi getKujonBackendApi()  {
        return kujonBackendApi;
    }

    private void createKujonBackendApi() {
        kujonBackendApi = retrofit.create(KujonBackendApi.class);
    }
}
