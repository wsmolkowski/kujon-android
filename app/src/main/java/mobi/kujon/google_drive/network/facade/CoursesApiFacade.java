package mobi.kujon.google_drive.network.facade;


import java.util.List;

import mobi.kujon.google_drive.model.json.CourseWithTerms;
import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.CoursesApiKujon;
import mobi.kujon.google_drive.network.unwrapped_api.CoursesApi;
import rx.Observable;

import static mobi.kujon.google_drive.network.api.ApiConst.getCacheValue;

public class CoursesApiFacade implements CoursesApi {

    private CoursesApiKujon coursesApiKujon;
    private BackendWrapper<List<CourseWithTerms>> backendWrapper;

    public CoursesApiFacade(CoursesApiKujon coursesApiKujon) {
        this.coursesApiKujon = coursesApiKujon;
        backendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<List<CourseWithTerms>> getCoursesBySemesters(boolean refresh) {
        return backendWrapper.doSomething(coursesApiKujon.coursesEditionsByTermRefresh(refresh, getCacheValue(refresh)));
    }
}
