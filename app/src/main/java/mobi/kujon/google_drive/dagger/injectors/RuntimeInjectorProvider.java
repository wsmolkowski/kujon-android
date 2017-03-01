package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.services.add_to_google_drive.AddToGoogleDriveService;
import mobi.kujon.google_drive.services.dowload_file.DowloadFileService;
import mobi.kujon.google_drive.services.upload.DowloadUploadFileServices;
import mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity;
import mobi.kujon.google_drive.ui.activities.file_details.FileDetailsActivity;

/**
 *
 */

public class RuntimeInjectorProvider implements InjectorProvider {

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

    @Override
    public Injector<AddToGoogleDriveService> provideAddToGooogleInjector() {
        return new UploadToDriveServiceInjector();
    }

    @Override
    public Injector<DowloadFileService> provideDowloadFileService() {
        return new DowloadServiceInjector();
    }
}
