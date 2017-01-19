package mobi.kujon.google_drive.network.api;

import java.util.List;

import mobi.kujon.network.json.KujonResponse;
import mobi.kujon.network.json.Term2;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

import static mobi.kujon.network.KujonBackendApi.X_KUJONREFRESH_TRUE;

/**
 *
 */

public interface SemesterApiKujon {
    @GET("terms")
    Observable<KujonResponse<List<Term2>>> getSemesters(@Header(X_KUJONREFRESH_TRUE) boolean refresh);
}
