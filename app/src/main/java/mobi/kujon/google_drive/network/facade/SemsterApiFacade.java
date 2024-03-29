package mobi.kujon.google_drive.network.facade;

import java.util.List;

import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.unwrapped_api.SemesterApi;
import mobi.kujon.google_drive.network.api.SemesterApiKujon;
import mobi.kujon.network.json.Term2;
import rx.Observable;

import static mobi.kujon.google_drive.network.api.ApiConst.getCacheValue;

/**
 *
 */

public class SemsterApiFacade implements SemesterApi {

    private SemesterApiKujon semesterApiKujon;
    private BackendWrapper<List<Term2>> listBackendWrapper;

    public SemsterApiFacade(SemesterApiKujon semesterApiKujon) {
        this.semesterApiKujon = semesterApiKujon;
        listBackendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<List<Term2>> getSemesters(boolean b) {
        return listBackendWrapper.doSomething(semesterApiKujon.getSemesters(b,getCacheValue(b)));
    }
}
