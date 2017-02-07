package mobi.kujon.google_drive.network.api;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import rx.Observable;

public interface FileDownloadKujon {
    @Streaming
    @GET("files/{fileId}")
    Observable<ResponseBody> downloadFile(@Path("fileId") String fileId);
}
