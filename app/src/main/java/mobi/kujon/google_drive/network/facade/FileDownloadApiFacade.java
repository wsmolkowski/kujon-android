package mobi.kujon.google_drive.network.facade;


import mobi.kujon.google_drive.network.api.FileDownloadKujon;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownloadApi;
import okhttp3.ResponseBody;
import rx.Observable;

public class FileDownloadApiFacade implements FileDownloadApi {

    private FileDownloadKujon fileDownloadKujon;

    public FileDownloadApiFacade(FileDownloadKujon fileDownloadKujon) {
        this.fileDownloadKujon = fileDownloadKujon;
    }

    @Override
    public Observable<ResponseBody> downloadFile(String fileId) {
        return fileDownloadKujon.downloadFile(fileId);
    }
}
