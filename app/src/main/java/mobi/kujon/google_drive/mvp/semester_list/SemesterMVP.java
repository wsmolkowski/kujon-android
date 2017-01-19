package mobi.kujon.google_drive.mvp.semester_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.SemesterDTO;
import mobi.kujon.google_drive.mvp.ClearSubscribtions;
import mobi.kujon.google_drive.mvp.HandleException;
import rx.Observable;

/**
 *
 */

public interface SemesterMVP {

    interface Presenter extends ClearSubscribtions{
        void askForSemester(boolean refresh);
    }
    interface View extends HandleException{
        void semeseterLoaded(List<SemesterDTO> list);
    }
    interface Model{
        Observable<List<SemesterDTO>> getListOfSemesters(boolean refresh);
    }
}
