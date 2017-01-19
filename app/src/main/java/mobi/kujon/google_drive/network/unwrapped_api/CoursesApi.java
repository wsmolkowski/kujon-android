package mobi.kujon.google_drive.network.unwrapped_api;


import java.util.List;
import java.util.SortedMap;

import mobi.kujon.network.json.Course;
import rx.Observable;

public interface CoursesApi {
    Observable<List<SortedMap<String, List<Course>>>> coursesEditionsByTermRefresh(boolean refresh);
}
