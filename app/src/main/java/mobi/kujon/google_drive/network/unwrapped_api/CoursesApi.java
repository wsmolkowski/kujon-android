package mobi.kujon.google_drive.network.unwrapped_api;


import android.support.v4.util.Pair;

import java.util.List;

import mobi.kujon.network.json.Course;
import rx.Observable;

public interface CoursesApi {
    Observable<List<Pair<String, List<Course>>>> getCoursesBySemesters(boolean refresh);
}
