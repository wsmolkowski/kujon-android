package mobi.kujon.google_drive.network.unwrapped_api;

import android.util.Pair;

import java.util.List;

import mobi.kujon.network.json.Course;
import mobi.kujon.network.json.Term2;
import retrofit2.http.Header;
import rx.Observable;

import static mobi.kujon.network.KujonBackendApi.X_KUJONREFRESH_TRUE;

/**
 *
 */

public interface SemesterApi {
    Observable<List<Term2>> getSemesters(boolean b);
}
