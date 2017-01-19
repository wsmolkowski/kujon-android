package mobi.kujon.google_drive.network.unwrapped_api;

import android.util.Pair;

import java.util.List;

import mobi.kujon.network.json.Course;
import retrofit2.http.Header;
import rx.Observable;

import static mobi.kujon.network.KujonBackendApi.X_KUJONREFRESH_TRUE;

/**
 *
 */

public interface SemesterApi {
    Observable<List<Pair<String, List<Course>>>> getListOfSubjcetsInSemester(@Header(X_KUJONREFRESH_TRUE) boolean b);
}
