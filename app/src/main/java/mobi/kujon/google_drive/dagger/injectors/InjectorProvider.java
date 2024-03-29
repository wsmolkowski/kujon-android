package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.services.add_to_google_drive.AddToGoogleDriveService;
import mobi.kujon.google_drive.services.dowload_file.DowloadFileService;
import mobi.kujon.google_drive.services.upload.DowloadUploadFileServices;
import mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity;
import mobi.kujon.google_drive.ui.activities.file_details.FileDetailsActivity;


/**
 *
 */

public interface InjectorProvider {
    FileActivityInjector provideFileActivityInjector();
    Injector<DowloadUploadFileServices> provideInjectorForService();
    Injector<ChooseStudentActivity> provideChooseStudentActivityInjector();
    Injector<FileDetailsActivity> provideFileDetailsActivityInjector();
    Injector<AddToGoogleDriveService> provideAddToGooogleInjector();
    Injector<DowloadFileService> provideDowloadFileService();
}
