package mobi.kujon.filesharing.model.courses_list;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.json.CourseWithTerms;
import mobi.kujon.google_drive.model.dto.CourseDTO;
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
        coursesModel.loadCourses(CoursesModelTestHelper.SEMESTER_ID, false)
                .subscribe(courseDTOs -> {
                    Assert.assertEquals(courseDTOs.size(), 1);
                    CourseDTO expected = courseDTOs.get(0);
                    CourseDTO actual = new CourseDTO(mockResponse.get(0).getCourses().get(0));
                    Assert.assertEquals(expected, actual);
                });
    }
}
