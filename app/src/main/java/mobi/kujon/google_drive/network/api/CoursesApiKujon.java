package mobi.kujon.google_drive.network.api;


import java.util.List;

import mobi.kujon.google_drive.model.CourseWithTerms;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

import static mobi.kujon.network.KujonBackendApi.X_KUJONREFRESH;

public interface CoursesApiKujon {

    String CACHE_CONTROL = "Cache-Control";

    @GET("courseseditionsbyterm")
    Observable<KujonResponse<List<CourseWithTerms>>> coursesEditionsByTermRefresh(@Header(X_KUJONREFRESH) boolean b,@Header(CACHE_CONTROL) String value);

}
