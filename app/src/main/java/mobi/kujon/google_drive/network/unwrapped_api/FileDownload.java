package mobi.kujon.google_drive.network.unwrapped_api;


import okhttp3.ResponseBody;
import rx.Observable;

public interface FileDownload {

    Observable<ResponseBody> downloadFile(String fileId);
}
