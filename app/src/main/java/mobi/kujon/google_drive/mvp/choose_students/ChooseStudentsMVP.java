package mobi.kujon.google_drive.mvp.choose_students;

import java.util.List;

import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_share.AskForStudentDto;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;
import rx.Observable;

/**
 *
 */

public interface ChooseStudentsMVP {

    interface Presenter extends ClearSubscriptions{
        void loadListOfStudents(AskForStudentDto askForStudentDto, boolean refresh);
    }

    interface Model{
        Observable<List<StudentShareDto>> provideListOfStudents(String courseId,String termId, boolean refresh);
    }

    interface View extends HandleException{
        void showStudentList(List<StudentShareDto> studentShareDtos);
    }
}
