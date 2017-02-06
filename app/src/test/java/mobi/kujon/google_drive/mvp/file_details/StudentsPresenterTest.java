package mobi.kujon.google_drive.mvp.file_details;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import mobi.kujon.CommonExceptionCreator;
import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StudentsPresenterTest extends UnitTest {
    @Mock
    FileDetailsMVP.FileDetailsFacade model;
    @Mock
    FileDetailsMVP.StudentsView view;

    private FileDetailsMVP.StudentsPresenter presenter;

    @Override
    protected void onSetup() {
        presenter = new StudentsPresenter(model, view, new SchedulersHolder(Schedulers.immediate(), Schedulers.immediate()));
    }

    @Test
    public void chooseEveryoneToShare() throws Exception {
        List<DisableableStudentShareDTO> students = provideStudentDtos(false, true);
        presenter.chooseEveryoneToShare(true, students);
        Mockito.verify(view).displayFileShares(students);
        assertFalse(students.get(0).isEnabled());
        assertTrue(students.get(0).isChosen());
    }

    @Test
    public void chooseEveryoneToShare2() throws Exception {
        List<DisableableStudentShareDTO> students = provideStudentDtos(true, true);
        presenter.chooseEveryoneToShare(true, students);
        Mockito.verify(view).displayFileShares(students);
        assertFalse(students.get(0).isEnabled());
        assertTrue(students.get(0).isChosen());
    }

    @Test
    public void chooseEveryoneToShare3() throws Exception {
        List<DisableableStudentShareDTO> students = provideStudentDtos(true, false);
        presenter.chooseEveryoneToShare(false, students);
        Mockito.verify(view).displayFileShares(students);
        assertTrue(students.get(0).isEnabled());
        assertTrue(students.get(0).isChosen());
    }


    @Test
    public void loadStudentsGoodData() throws Exception {
        String fileId = "1";
        boolean refresh = true;
        List<DisableableStudentShareDTO> studentShareDTOs = provideStudentDtos(false, true);
        Mockito.when(model.loadStudentShares(fileId, refresh))
                .thenReturn(Observable.just(studentShareDTOs));
        presenter.loadStudents(fileId, refresh);
        Mockito.verify(view).displayFileShares(studentShareDTOs);
    }

    @Test
    public void loadStudentsBadData() throws Exception {
        String fileId = "1";
        boolean refresh = true;
        NullPointerException nullPointerException = CommonExceptionCreator.provideNullPointer(" Jakis blad");
        Mockito.when(model.loadStudentShares(fileId, refresh))
                .thenReturn(Observable.error(nullPointerException));
        presenter.loadStudents(fileId, refresh);
        Mockito.verify(view).handleException(nullPointerException);
    }

    private List<DisableableStudentShareDTO> provideStudentDtos(boolean isChosen, boolean isEnabled) {
        return Collections.singletonList(new DisableableStudentShareDTO("John", "1", isChosen, isEnabled));
    }

}