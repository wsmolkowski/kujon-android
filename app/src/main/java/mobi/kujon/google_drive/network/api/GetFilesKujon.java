package mobi.kujon.google_drive.network.api;

import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

import static mobi.kujon.google_drive.network.api.ApiConst.CACHE_CONTROL;
import static mobi.kujon.google_drive.network.api.ApiConst.KUJONREFRESH;

/**
 *
 */

public interface GetFilesKujon {


    @GET("files")
    Observable<KujonResponse<List<KujonFile>>> getAllFiles(@Header(KUJONREFRESH) boolean b, @Header(CACHE_CONTROL) String value);

    @GET("files")
    Observable<KujonResponse<List<KujonFile>>> getFiles(@Header(KUJONREFRESH) boolean b, @Header(CACHE_CONTROL) String value,@Query("course_id") String courseId, @Query("term_id") String termId);
}
