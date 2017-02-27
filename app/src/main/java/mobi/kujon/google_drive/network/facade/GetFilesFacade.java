package mobi.kujon.google_drive.network.facade;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import mobi.kujon.google_drive.model.json.KujonFile;
import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.ApiConst;
import mobi.kujon.google_drive.network.api.DeleteFileKujon;
import mobi.kujon.google_drive.network.api.GetFilesKujon;
import mobi.kujon.google_drive.network.unwrapped_api.DeleteFileApi;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import rx.Observable;

/**
 *
 */

public class GetFilesFacade implements GetFilesApi, DeleteFileApi {

    private GetFilesKujon getFilesKujon;
    private BackendWrapper<List<KujonFile>> listBackendWrapper;
    private BackendWrapper<String> deleteWrapper;
    private ConcurrentHashMap<String, List<KujonFile>> kujonFile;
    private DeleteFileKujon deleteFileKujon;

    public GetFilesFacade(GetFilesKujon getFilesKujon, DeleteFileKujon deleteFileKujon) {
        this.getFilesKujon = getFilesKujon;
        listBackendWrapper = new BackendWrapper<>();
        deleteWrapper = new BackendWrapper<>();
        kujonFile = new ConcurrentHashMap<>();
        this.deleteFileKujon = deleteFileKujon;
    }

    @Override
    public Observable<List<KujonFile>> getAllFiles(boolean refresh) {
        return listBackendWrapper.doSomething(getFilesKujon.getAllFiles(refresh, ApiConst.getCacheValue(refresh)));
    }

    @Override
    public Observable<List<KujonFile>> getFiles(boolean refresh, String courseId, String termId) {
        String courseTermKey = courseId + termId;
        if (!refresh && kujonFile.containsKey(courseTermKey)) {
            return Observable.just(kujonFile.get(courseTermKey));
        }
        return listBackendWrapper
                .doSomething(getFilesKujon.getFiles(ApiConst.getCacheValue(refresh), courseId, termId))
                .doOnNext(it -> kujonFile.put(courseTermKey, it));
    }

    @Override
    public Observable<String> deleteFile(String fileId, String courseId, String termId) {
        String courseTermKey = courseId + termId;
        return deleteWrapper.doSomething(deleteFileKujon.deleteFile(fileId))
                .doOnNext(it -> {
                    for (KujonFile kujonF : kujonFile.get(courseTermKey)) {
                        if (kujonF.fileId.equals(fileId)) {
                            kujonFile.get(courseTermKey).remove(kujonF);
                            break;
                        }
                    }
                });
    }
}
