package mobi.kujon.filesharing.presenters.semester_list;


import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.dto.SemesterDTO;
import mobi.kujon.google_drive.mvp.semester_list.SemestersMVP;
import mobi.kujon.google_drive.mvp.semester_list.SemestersPresenter;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.schedulers.Schedulers;

import static mobi.kujon.CommonExceptionCreator.provideHTTPException;
import static mobi.kujon.CommonExceptionCreator.provideNullPointer;
import static mobi.kujon.filesharing.presenters.semester_list.SemestersPresenterTestHelper.getSemestersResponse;

public class SemestersPresenterTest extends UnitTest {

    private static final String NEW_API_ERROR = "new api error";

    @Mock
    SemestersMVP.Model model;

    @Mock
    SemestersMVP.View view;

    private SemestersPresenter semestersPresenter;

    @Override
    protected void onSetup() {
        SchedulersHolder schedulersHolder = new SchedulersHolder(Schedulers.immediate(), Schedulers.immediate());
        semestersPresenter = new SemestersPresenter(view, model, schedulersHolder);
    }

    @Test
    public void testGetSemestersNoErrors() {
        List<SemesterDTO> semesters = getSemestersResponse(false);
        Mockito.when(model.getListOfSemesters(false)).thenReturn(Observable.just(semesters));
        semestersPresenter.askForSemesters(false);
        Mockito.verify(view).semesetersLoaded(semesters);
    }

    @Test
    public void testGetSemestersInformViewOfError() {
        HttpException exception = provideHTTPException(NEW_API_ERROR);
        Mockito.when(model.getListOfSemesters(false)).thenReturn(Observable.error(exception));
        semestersPresenter.askForSemesters(false);
        Mockito.verify(view).handleException(exception);
        NullPointerException nullPointerException = provideNullPointer(NEW_API_ERROR);
        Mockito.when(model.getListOfSemesters(false)).thenReturn(Observable.error(nullPointerException));
        semestersPresenter.askForSemesters(false);
        Mockito.verify(view).handleException(nullPointerException);
    }


}
