package mobi.kujon.google_drive.dagger;

import com.google.gson.Gson;

import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.network.unwrapped_api.CoursesApi;
import mobi.kujon.google_drive.network.unwrapped_api.DeleteFileApi;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownloadApi;
import mobi.kujon.google_drive.network.unwrapped_api.FileUpload;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import mobi.kujon.google_drive.network.unwrapped_api.SemesterApi;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFileApi;
import mobi.kujon.google_drive.network.unwrapped_api.CourseDetailsApi;
import mobi.kujon.google_drive.services.ServiceOpener;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import mobi.kujon.google_drive.utils.TempFileCreator;
import mobi.kujon.utils.user_data.UserDataFacade;

/**
 *
 */
public interface FilesComponent {
    SchedulersHolder provideSchedulersHolder();
    SemesterApi provideSemesterApi();
    CoursesApi provideCoursesApi() ;
    GetFilesApi provideGetFiles();
    ShareFileApi provideShareFile() ;
    FileDownloadApi provideFileDownload() ;
    DeleteFileApi provideDeleteFile() ;
    FileUpload provideUploadFile() ;
    CourseDetailsApi provideCourseDetailsApi();
    FileStreamUpdateMVP.Model provideModel();
    FileStreamUpdateMVP.CancelModel provideCancelModel();
    Gson provideGson();
    ServiceOpener provideServiceOpener();
    UserDataFacade provideUserDataFacade();
    TempFileCreator provideTmpFileCreator();
}
