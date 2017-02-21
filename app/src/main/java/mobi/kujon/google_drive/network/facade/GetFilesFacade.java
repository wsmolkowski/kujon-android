package mobi.kujon.google_drive.network.facade;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import mobi.kujon.google_drive.model.json.KujonFile;
import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.ApiConst;
import mobi.kujon.google_drive.network.api.GetFilesKujon;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import rx.Observable;

/**
 *
 */

public class GetFilesFacade implements GetFilesApi {

    private GetFilesKujon getFilesKujon;
    private BackendWrapper<List<KujonFile>> listBackendWrapper;
    private ConcurrentHashMap<String,List<KujonFile>> kujonFile;
    public GetFilesFacade(GetFilesKujon getFilesKujon) {
        this.getFilesKujon = getFilesKujon;
        listBackendWrapper = new BackendWrapper<>();
        kujonFile = new ConcurrentHashMap<>();
    }

    @Override
    public Observable<List<KujonFile>> getAllFiles(boolean refresh) {
        return listBackendWrapper.doSomething(getFilesKujon.getAllFiles(refresh, ApiConst.getCacheValue(refresh)));
    }

    @Override
    public Observable<List<KujonFile>> getFiles(boolean refresh,String courseId, String termId) {
        String courseTermKey = courseId + termId;
        if(!refresh && kujonFile.containsKey(courseTermKey)){
            return Observable.just(kujonFile.get(courseTermKey));
        }
        return listBackendWrapper
                .doSomething(getFilesKujon.getFiles(refresh, ApiConst.getCacheValue(refresh),courseId,termId))
                .doOnNext(it-> kujonFile.put(courseTermKey,it));
    }
}
