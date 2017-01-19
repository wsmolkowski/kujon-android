package mobi.kujon.filesharing.model.courses_list;


import android.support.v4.util.Pair;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.mvp.courses_list.CoursesModel;
import mobi.kujon.google_drive.network.facade.CoursesApiFacade;
import mobi.kujon.network.json.Course;
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
        List<Pair<String, List<Course>>> mockResponse = CoursesModelTestHelper.mockCoursesBySemesters();
        Mockito.when(coursesApiFacade.getCoursesBySemesters(Mockito.anyBoolean())).thenReturn(Observable.just(mockResponse));
        coursesModel.loadCourses(CoursesModelTestHelper.SEMESTER_ID, false)
                .subscribe(courseDTOs -> {
                    Assert.assertEquals(courseDTOs.size(), 1);
                    Assert.assertEquals(courseDTOs.get(0).getCourseId(), CoursesModelTestHelper.COURSE_ID);
                    Assert.assertEquals(courseDTOs.get(0).getCourseName(), CoursesModelTestHelper.COURSE_NAME);
                });
    }
}
