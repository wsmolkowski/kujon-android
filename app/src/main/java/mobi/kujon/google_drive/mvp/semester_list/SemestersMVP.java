package mobi.kujon.google_drive.mvp.semester_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.SemesterDTO;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;
import rx.Observable;

/**
 *
 */

public interface SemestersMVP {

    interface Presenter extends ClearSubscriptions {
        void askForSemesters(boolean refresh);
    }
    interface View extends HandleException {
        void semestersLoaded(List<SemesterDTO> list);
    }
    interface Model{
        Observable<List<SemesterDTO>> getListOfSemesters(boolean refresh);
    }
}
