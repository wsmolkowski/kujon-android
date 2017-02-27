package mobi.kujon.filesharing.model.courses_list;


import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.json.CourseWithTerms;
import mobi.kujon.google_drive.mvp.courses_list.CoursesModel;
import mobi.kujon.google_drive.network.facade.CoursesApiFacade;
import rx.Observable;

public class CoursesModelTest extends UnitTest {

    @Mock
    CoursesApiFacade coursesApiFacade;

    private CoursesModel coursesModel;

    @Override
    protected void onSetup() {
        coursesModel = new CoursesModel(coursesApiFacade);
    }

    @Test
    public void testGetCourses() {
        List<CourseWithTerms> mockResponse = CoursesModelTestHelper.mockCoursesBySemesters();
        Mockito.when(coursesApiFacade.getCoursesBySemesters(Mockito.anyBoolean())).thenReturn(Observable.just(mockResponse));

    }
}
