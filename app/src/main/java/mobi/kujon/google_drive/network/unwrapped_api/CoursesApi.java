package mobi.kujon.google_drive.network.unwrapped_api;


import java.util.List;

import mobi.kujon.google_drive.model.json.CourseWithTerms;
import rx.Observable;

public interface CoursesApi {
    Observable<List<CourseWithTerms>> getCoursesBySemesters(boolean refresh);
}
