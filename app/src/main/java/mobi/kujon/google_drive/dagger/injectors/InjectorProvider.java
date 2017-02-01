package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity;
import mobi.kujon.google_drive.services.DowloadUploadFileServices;
import mobi.kujon.google_drive.ui.activities.courses.CoursesInSemseterActivity;
import mobi.kujon.google_drive.ui.activities.semesters.SemestersActivity;


/**
 *
 */

public interface InjectorProvider {
    Injector<SemestersActivity> provideInjector();
    Injector<CoursesInSemseterActivity> provideCourseInSemesterInjector();
    FileActivityInjector provideFileActivityInjector();
    Injector<DowloadUploadFileServices> provideInjectorForService();

    Injector<ChooseStudentActivity> provideChooseStudentActivityInjector();
}
