package mobi.kujon.google_drive.network.facade;


import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownloadApi;
import mobi.kujon.google_drive.network.api.FileDownloadKujon;
import okhttp3.ResponseBody;
import rx.Observable;

public class FileDownloadApiFacade implements FileDownloadApi {

    private FileDownloadKujon fileDownloadKujon;
    private BackendWrapper<ResponseBody> responseBodyBackendWrapper;

    public FileDownloadApiFacade(FileDownloadKujon fileDownloadKujon) {
        this.fileDownloadKujon = fileDownloadKujon;
        responseBodyBackendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<ResponseBody> downloadFile(String fileId) {
        return responseBodyBackendWrapper.doSomething(fileDownloadKujon.downloadFile(fileId));
    }
}
