package mobi.kujon.google_drive;


import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.network.json.KujonResponse;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface KujonFilesharingApi {

    @GET("files")
    Observable<KujonResponse<List<KujonFile>>> getFilesList();

    @DELETE("files/{fileId}")
    Observable<KujonResponse<String>> deleteFile(@Path("fileId") String fileId);

    @GET("files/{fileId}")
    Observable<KujonResponse<ResponseBody>> downloadFile(@Path("fileId") String fileId);
}
