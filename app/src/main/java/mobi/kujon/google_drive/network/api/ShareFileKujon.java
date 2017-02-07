package mobi.kujon.google_drive.network.api;


import java.util.List;

import mobi.kujon.google_drive.model.json.ShareFileTarget;
import mobi.kujon.google_drive.model.json.SharedFile;
import mobi.kujon.network.json.KujonResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface ShareFileKujon {
    @POST("filesshare")
    Observable<KujonResponse<SharedFile>> shareFile(@Body List<ShareFileTarget> shareFileTargets);
}
