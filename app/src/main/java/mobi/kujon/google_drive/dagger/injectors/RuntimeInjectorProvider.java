package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.services.upload.DowloadUploadFileServices;
import mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity;
import mobi.kujon.google_drive.ui.activities.courses.CoursesInSemseterActivity;
import mobi.kujon.google_drive.ui.activities.file_details.FileDetailsActivity;
import mobi.kujon.google_drive.ui.activities.semesters.SemestersActivity;

/**
 *
 */

public class RuntimeInjectorProvider implements InjectorProvider {
    @Override
    public Injector<SemestersActivity> provideInjector() {
        return new SemesterActivityInjector();
    }

    @Override
    public Injector<CoursesInSemseterActivity> provideCourseInSemesterInjector() {
        return new CourseInSemseterInjector();
    }

    @Override
    public FileActivityInjector provideFileActivityInjector() {
        return new FileActivityInjector();
    }

    @Override
    public Injector<DowloadUploadFileServices> provideInjectorForService() {
        return new UploadServiceInjector();
    }

    @Override
    public Injector<ChooseStudentActivity> provideChooseStudentActivityInjector() {
        return new ChooseStudentInjector();
    }

    @Override
    public Injector<FileDetailsActivity> provideFileDetailsActivityInjector() {
        return new FileDetailsInjector();
    }
}
