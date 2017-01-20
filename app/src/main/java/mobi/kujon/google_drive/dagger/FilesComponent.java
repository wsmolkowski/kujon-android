package mobi.kujon.google_drive.dagger;

import mobi.kujon.KujonApplication;
import mobi.kujon.google_drive.network.unwrapped_api.CoursesApi;
import mobi.kujon.google_drive.network.unwrapped_api.DeleteFile;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownload;
import mobi.kujon.google_drive.network.unwrapped_api.FileUpload;
import mobi.kujon.google_drive.network.unwrapped_api.GetFiles;
import mobi.kujon.google_drive.network.unwrapped_api.SemesterApi;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFile;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */
public interface FilesComponent {
    void inject(KujonApplication kujonApplication);
    SchedulersHolder provideSchedulersHolder();
    SemesterApi provideSemesterApi();
    CoursesApi provideCoursesApi() ;
    GetFiles provideGetFiles();
    ShareFile provideShareFile() ;
    FileDownload provideFileDownload() ;
    DeleteFile provideDeleteFile() ;
    FileUpload provideUploadFile() ;
}
