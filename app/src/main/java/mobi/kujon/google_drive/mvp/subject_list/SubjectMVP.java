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
        void onSubjectsLoaded(List<SubjectDTO> subjectDTOs);
    }

    interface Presenter extends ClearSubscribtions {
        void loadListOfSubjectsForSemester(String semesterId, boolean refresh);
    }

    interface Model {
        Observable<SubjectDTO> loadListOfSubjects(String semesterId, boolean refresh);
    }
}
