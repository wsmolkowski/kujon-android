package mobi.kujon.google_drive.mvp.choose_students;

import java.util.List;

import mobi.kujon.google_drive.model.ShareFileTargetType;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import rx.Observable;

/**
 *
 */

public interface ChooseStudentsMVP {

    interface Presenter{
        void loadListOfStudents(String courseId,String termId,boolean refresh);
    }

    interface FilesPresenter{
        void loadListOfStudents(String courseId, String termId, @ShareFileTargetType String shareType,List<Integer> studentIds);
    }

    interface SharePresenter extends ClearSubscriptions{
        void shareFile(FileShareDto fileShareDto);
    }

    interface Model{
        Observable<List<StudentShareDto>> provideListOfStudents(String courseId,String termId);
    }

    interface View{
        void showStudentList(List<StudentShareDto> studentShareDtos);
    }
}
