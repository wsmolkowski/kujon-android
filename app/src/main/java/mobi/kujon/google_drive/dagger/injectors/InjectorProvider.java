package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.services.add_to_google_drive.AddToGoogleDriveService;
import mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity;
import mobi.kujon.google_drive.services.upload.DowloadUploadFileServices;
import mobi.kujon.google_drive.ui.activities.courses.CoursesInSemseterActivity;
import mobi.kujon.google_drive.ui.activities.file_details.FileDetailsActivity;
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
    Injector<FileDetailsActivity> provideFileDetailsActivityInjector();
    Injector<AddToGoogleDriveService> provideAddToGooogleInjector();
}
