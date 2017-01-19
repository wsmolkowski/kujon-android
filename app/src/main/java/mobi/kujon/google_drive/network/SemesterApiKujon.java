package mobi.kujon.google_drive.network;

import android.util.Pair;

import java.util.List;

import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

import static mobi.kujon.network.KujonBackendApi.X_KUJONREFRESH_TRUE;

/**
 *
 */

public interface SemesterApiKujon {
    @GET("courseseditionsbyterm")
    Observable<KujonResponse<List<Pair<String, List<Course>>>>> getListOfSubjcetsInSemester(@Header(X_KUJONREFRESH_TRUE) boolean b);
}
