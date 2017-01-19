package mobi.kujon.google_drive.network.facade;

import android.util.Pair;

import java.util.List;

import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.unwrapped_api.SemesterApi;
import mobi.kujon.google_drive.network.api.SemesterApiKujon;
import mobi.kujon.network.json.Course;
import rx.Observable;

/**
 *
 */

public class SemsterApiFacade implements SemesterApi {

    private SemesterApiKujon semesterApiKujon;
    private BackendWrapper<List<Pair<String, List<Course>>>> listBackendWrapper;

    public SemsterApiFacade(SemesterApiKujon semesterApiKujon) {
        this.semesterApiKujon = semesterApiKujon;
        listBackendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<List<Pair<String, List<Course>>>> getListOfSubjcetsInSemester(boolean b) {
        return listBackendWrapper.doSmething(semesterApiKujon.getListOfSubjcetsInSemester(b));
    }
}
