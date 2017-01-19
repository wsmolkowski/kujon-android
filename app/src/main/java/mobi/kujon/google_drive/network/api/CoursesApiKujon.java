package mobi.kujon.google_drive.network.api;


import java.util.List;
import java.util.SortedMap;

import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

import static mobi.kujon.network.KujonBackendApi.X_KUJONREFRESH_TRUE;

public interface CoursesApiKujon {
    @GET("courseseditionsbyterm")
    Observable<KujonResponse<List<SortedMap<String, List<Course>>>>> coursesEditionsByTermRefresh(@Header(X_KUJONREFRESH_TRUE) boolean b);

}
