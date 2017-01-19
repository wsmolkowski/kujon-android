package mobi.kujon.google_drive.network.facade;


import java.util.List;
import java.util.SortedMap;

import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.CoursesApiKujon;
import mobi.kujon.google_drive.network.unwrapped_api.CoursesApi;
import mobi.kujon.network.json.Course;
import rx.Observable;

public class CoursesApiFacade implements CoursesApi {

    private CoursesApiKujon coursesApiKujon;
    private BackendWrapper<List<SortedMap<String, List<Course>>>> backendWrapper;

    public CoursesApiFacade(CoursesApiKujon coursesApiKujon) {
        this.coursesApiKujon = coursesApiKujon;
    }

    @Override
    public Observable<List<SortedMap<String, List<Course>>>> coursesEditionsByTermRefresh(boolean refresh) {
        return backendWrapper.doSomething(coursesApiKujon.coursesEditionsByTermRefresh(refresh));
    }
}
