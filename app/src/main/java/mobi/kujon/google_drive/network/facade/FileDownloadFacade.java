package mobi.kujon.google_drive.network.facade;


import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.FileDownload;
import mobi.kujon.google_drive.network.FileDownloadKujon;
import okhttp3.ResponseBody;
import rx.Observable;

public class FileDownloadFacade implements FileDownload {

    private FileDownloadKujon fileDownloadKujon;
    private BackendWrapper<ResponseBody> responseBodyBackendWrapper;

    public FileDownloadFacade(FileDownloadKujon fileDownloadKujon) {
        this.fileDownloadKujon = fileDownloadKujon;
        responseBodyBackendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<ResponseBody> downloadFile(String fileId) {
        return responseBodyBackendWrapper.doSmething(fileDownloadKujon.downloadFile(fileId));
    }
}
