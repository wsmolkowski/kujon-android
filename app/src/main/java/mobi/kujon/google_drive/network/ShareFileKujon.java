package mobi.kujon.google_drive.network;


import mobi.kujon.google_drive.model.ShareFileTarget;
import mobi.kujon.google_drive.model.SharedFile;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface ShareFileKujon {
    @POST("filesshare")
    Observable<KujonResponse<SharedFile>> shareFile(@Body ShareFileTarget shareFileTarget);
}
