package mobi.kujon.google_drive.mvp.courses_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.CourseDTO;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;
import rx.Observable;

/**
 *
 */

public interface CoursesMVP {
    interface View extends HandleException {
        void onCoursesLoaded(List<CourseDTO> courseDTOs);
    }

    interface Presenter extends ClearSubscriptions {
        void loadCoursesForSemester(String semesterId, boolean refresh);
    }

    interface Model {
        Observable<List<CourseDTO>> loadCourses(String semesterId, boolean refresh);
    }
}
