package mobi.kujon.google_drive.mvp.courses_list;


import java.util.List;

import mobi.kujon.google_drive.model.dto.CourseDTO;
import rx.Observable;

public class CoursesModel implements CoursesMVP.Model {
    @Override
    public Observable<List<CourseDTO>> loadCourses(String semesterId, boolean refresh) {
        return null;
    }
}
