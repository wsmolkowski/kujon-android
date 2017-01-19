package mobi.kujon.google_drive.mvp.subject_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.SubjectDTO;
import mobi.kujon.google_drive.mvp.ClearSubscribtions;
import mobi.kujon.google_drive.mvp.HandleException;
import rx.Observable;

/**
 *
 */

public interface SubjectMVP {
    interface View extends HandleException {
        void onCoursesLoaded(List<SubjectDTO> subjectDTOs);
    }

    interface Presenter extends ClearSubscribtions {
        void loadCoursesForSemester(String semesterId, boolean refresh);
    }

    interface Model {
        Observable<List<SubjectDTO>> loadCourses(String semesterId, boolean refresh);
    }
}
