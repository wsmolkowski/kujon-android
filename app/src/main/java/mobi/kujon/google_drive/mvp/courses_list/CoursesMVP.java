package mobi.kujon.google_drive.mvp.courses_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.TermWithCourseDTO;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;
import rx.Observable;

/**
 *
 */

public interface CoursesMVP {
    interface View extends HandleException {
        void onCoursesTermsLoaded(List<TermWithCourseDTO> courseDTOs);
    }

    interface Presenter extends ClearSubscriptions {
        void loadTermsInCourses( boolean refresh);
    }

    interface Model {
        Observable<List<TermWithCourseDTO>> loadCourses(boolean refresh);
    }
}
