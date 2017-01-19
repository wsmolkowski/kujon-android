package mobi.kujon.google_drive.network;

import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 */

public interface GetFilesKujon {


    @GET("files")
    Observable<KujonResponse<List<KujonFile>>> getAllFiles();

    @GET("files")
    Observable<KujonResponse<List<KujonFile>>> getFiles(@Query("course_id") String courseId, @Query("term_id") String termId);
}
