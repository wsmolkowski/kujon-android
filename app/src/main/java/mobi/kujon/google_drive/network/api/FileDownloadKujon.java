package mobi.kujon.google_drive.network.api;


import mobi.kujon.network.json.KujonResponse;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import rx.Observable;

public interface FileDownloadKujon {
    @Streaming
    @GET("files/{fileId}")
    Observable<KujonResponse<ResponseBody>> downloadFile(@Path("fileId") String fileId);
}
