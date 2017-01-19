package mobi.kujon.google_drive.network.facade;

import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.unwrapped_api.GetFiles;
import mobi.kujon.google_drive.network.api.GetFilesKujon;
import rx.Observable;

/**
 *
 */

public class GetFilesFacade implements GetFiles {

    private GetFilesKujon getFilesKujon;
    private BackendWrapper<List<KujonFile>> listBackendWrapper;
    public GetFilesFacade(GetFilesKujon getFilesKujon) {
        this.getFilesKujon = getFilesKujon;
        listBackendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<List<KujonFile>> getAllFiles() {
        return listBackendWrapper.doSomething(getFilesKujon.getAllFiles());
    }

    @Override
    public Observable<List<KujonFile>> getFiles(String courseId, String termId) {
        return listBackendWrapper.doSomething(getFilesKujon.getFiles(courseId,termId));
    }
}
