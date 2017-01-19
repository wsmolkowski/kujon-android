package mobi.kujon.filesharing.presenters.courses_list;


import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.dto.CourseDTO;
import mobi.kujon.google_drive.mvp.courses_list.CoursesMVP;
import mobi.kujon.google_drive.mvp.courses_list.CoursesPresenter;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.schedulers.Schedulers;

import static mobi.kujon.CommonExceptionCreator.provideHTTPException;
import static mobi.kujon.CommonExceptionCreator.provideNullPointer;
import static mobi.kujon.filesharing.presenters.courses_list.CoursesPresenterTestHelper.*;

public class CoursesPresenterTest extends UnitTest {

    private static final String SEMESTER_ID = "1";
    private static final String NEW_API_ERROR = "new api error";

    @Mock
    CoursesMVP.Model model;

    @Mock
    CoursesMVP.View view;

    private CoursesPresenter coursesPresenter;

    @Override
    protected void onSetup() {
        SchedulersHolder holder = new SchedulersHolder(Schedulers.immediate(), Schedulers.immediate());
        coursesPresenter = new CoursesPresenter(model, view, holder);
    }

    @Test
    public void testGetCoursesNoError() {
        List<CourseDTO> courses = mockCourses();
        Mockito.when(model.loadCourses(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(Observable.just(courses));
        coursesPresenter.loadCoursesForSemester(SEMESTER_ID, false);
        Mockito.verify(view).onCoursesLoaded(courses);
    }

    @Test
    public void testGetCoursesInformViewOfErron() {
        HttpException exception = provideHTTPException(NEW_API_ERROR);
        Mockito.when(model.loadCourses(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(Observable.error(exception));
        coursesPresenter.loadCoursesForSemester(SEMESTER_ID, false);
        Mockito.verify(view).handleException(exception);
        NullPointerException nullPointerException = provideNullPointer(NEW_API_ERROR);
        Mockito.when(model.loadCourses(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(Observable.error(nullPointerException));
        coursesPresenter.loadCoursesForSemester(SEMESTER_ID, false);
        Mockito.verify(view).handleException(nullPointerException);
    }


}
