package mobi.kujon.google_drive.network.facade;

import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.ApiConst;
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
    public Observable<List<KujonFile>> getAllFiles(boolean refresh) {
        return listBackendWrapper.doSomething(getFilesKujon.getAllFiles(refresh, ApiConst.getCacheValue(refresh)));
    }

    @Override
    public Observable<List<KujonFile>> getFiles(boolean refresh,String courseId, String termId) {
        return listBackendWrapper.doSomething(getFilesKujon.getFiles(refresh, ApiConst.getCacheValue(refresh),courseId,termId));
    }
}
