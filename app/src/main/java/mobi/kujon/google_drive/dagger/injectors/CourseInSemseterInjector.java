package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.ui.activities.courses.CoursesInSemseterActivity;
import mobi.kujon.google_drive.ui.activities.courses.dagger.CoursesInSemesterModule;
import mobi.kujon.google_drive.ui.activities.courses.dagger.DaggerRuntimeCourseInSemesterActivityComponent;

/**
 *
 */

public class CourseInSemseterInjector extends AbstractInjector<CoursesInSemseterActivity> {
    @Override
    public void inject(CoursesInSemseterActivity injectTo) {
        DaggerRuntimeCourseInSemesterActivityComponent.builder()
                .filesComponent(getFilesComponent(injectTo))
                .coursesInSemesterModule(new CoursesInSemesterModule(injectTo))
                .build().inject(injectTo);
    }
}
