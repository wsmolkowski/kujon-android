package mobi.kujon.filesharing.presenters.courses_list;


import org.mockito.Mock;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.mvp.courses_list.CoursesMVP;
import mobi.kujon.google_drive.mvp.courses_list.CoursesPresenter;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.schedulers.Schedulers;

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

}
