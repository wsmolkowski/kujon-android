package mobi.kujon.google_drive.network.api;


import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

import static mobi.kujon.google_drive.network.api.ApiConst.KUJONREFRESH;

public interface CourseDetailsApiKujon {

    @GET("courseseditions/{courseId}/{termId}")
    Observable<KujonResponse<CourseDetails>> getCourseDetails(@Header(KUJONREFRESH) boolean refresh, @Path("courseId") String courseId, @Path("termId") String termId);

}
