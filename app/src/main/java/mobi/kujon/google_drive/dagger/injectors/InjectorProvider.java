package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.ui.activities.courses.CoursesInSemseterActivity;
import mobi.kujon.google_drive.ui.activities.semesters.SemestersActivity;


/**
 *
 */

public interface InjectorProvider {
    Injector<SemestersActivity> provideInjector();
    Injector<CoursesInSemseterActivity> provideCourseInSemesterInjector();
    FileActivityInjector provideFileActivityInjector();

}
