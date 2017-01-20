package mobi.kujon.google_drive.ui.activities.courses.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.courses_list.CoursesMVP;
import mobi.kujon.google_drive.mvp.courses_list.CoursesModel;
import mobi.kujon.google_drive.mvp.courses_list.CoursesPresenter;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

@Module
public class CoursesInSemesterModule {

    private CoursesMVP.View view;

    public CoursesInSemesterModule(CoursesMVP.View view) {
        this.view = view;
    }

    @Provides
    @ActivityScope
    CoursesMVP.Presenter providePresenter(CoursesModel coursesModel, SchedulersHolder schedulersHolder){
        return new CoursesPresenter(coursesModel,view,schedulersHolder);
    }
}
