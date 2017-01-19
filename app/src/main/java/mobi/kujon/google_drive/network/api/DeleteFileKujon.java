package mobi.kujon.google_drive.network.api;


import mobi.kujon.network.json.KujonResponse;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import rx.Observable;

public interface DeleteFileKujon {
    @DELETE("files/{fileId}")
    Observable<KujonResponse<String>> deleteFile(@Path("fileId") String fileId);
}
