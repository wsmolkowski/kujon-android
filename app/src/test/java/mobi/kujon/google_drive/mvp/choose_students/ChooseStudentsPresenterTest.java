package mobi.kujon.google_drive.mvp.choose_students;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import mobi.kujon.CommonExceptionCreator;
import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_share.AskForStudentDto;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.schedulers.Schedulers;

public class ChooseStudentsPresenterTest extends UnitTest {

    @Mock
    ChooseStudentsMVP.Model model;

    @Mock
    ChooseStudentsMVP.View view;

    private ChooseStudentsMVP.Presenter presenter;

    @Override
    protected void onSetup() {
        presenter = new ChooseStudentsPresenter(model, view, new SchedulersHolder(Schedulers.immediate(), Schedulers.immediate()));
    }

    @Test
    public void testCorrectData() {
        List<StudentShareDto> students = provideStudentShareDtos();
        Mockito.when(model.provideListOfStudents(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(Observable.just(students));
        presenter.loadListOfStudents(provideAskForStudentsDto(), true);
        Mockito.verify(view).showStudentList(students);
    }

    @Test
    public void testException() {
        NullPointerException nullPointerException = CommonExceptionCreator.provideNullPointer(" Jakis blad");
        Mockito.when(model.provideListOfStudents(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(Observable.error(nullPointerException));
        presenter.loadListOfStudents(provideAskForStudentsDto(), true);
        Mockito.verify(view).handleException(nullPointerException);
    }

    public static List<StudentShareDto> provideStudentShareDtos() {
        return Collections.singletonList(new StudentShareDto("Bill Clinton", "id", false));
    }

    public static AskForStudentDto provideAskForStudentsDto() {
        return new AskForStudentDto("cid", "tid", provideStudentShareDtos().get(0));
    }
}