package mobi.kujon.google_drive.network.unwrapped_api;


import android.util.Pair;

import java.util.List;

import mobi.kujon.network.json.Course;
import rx.Observable;

public interface CoursesApi {
    Observable<List<Pair<String, List<Course>>>> coursesEditionsByTermRefresh(boolean refresh);
}
