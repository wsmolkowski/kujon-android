package mobi.kujon.google_drive.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.GoogleDriveScope;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.network.api.CourseDetailsApiKujon;
import mobi.kujon.google_drive.network.api.CoursesApiKujon;
import mobi.kujon.google_drive.network.api.DeleteFileKujon;
import mobi.kujon.google_drive.network.api.FileDownloadKujon;
import mobi.kujon.google_drive.network.api.FileUploadKujon;
import mobi.kujon.google_drive.network.api.GetFilesKujon;
import mobi.kujon.google_drive.network.api.SemesterApiKujon;
import mobi.kujon.google_drive.network.api.ShareFileKujon;
import mobi.kujon.google_drive.network.facade.CourseDetailsFacade;
import mobi.kujon.google_drive.network.facade.CoursesApiFacade;
import mobi.kujon.google_drive.network.facade.FileDownloadApiFacade;
import mobi.kujon.google_drive.network.facade.FileUploadApiFacade;
import mobi.kujon.google_drive.network.facade.GetFilesFacade;
import mobi.kujon.google_drive.network.facade.SemsterApiFacade;
import mobi.kujon.google_drive.network.facade.ShareFileFacade;
import mobi.kujon.google_drive.network.unwrapped_api.CourseDetailsApi;
import mobi.kujon.google_drive.network.unwrapped_api.CoursesApi;
import mobi.kujon.google_drive.network.unwrapped_api.DeleteFileApi;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownloadApi;
import mobi.kujon.google_drive.network.unwrapped_api.FileUploadApi;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import mobi.kujon.google_drive.network.unwrapped_api.SemesterApi;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFileApi;
import mobi.kujon.google_drive.utils.MultipartUtils;
import retrofit2.Retrofit;

/**
 *
 */


@Module
public class FilesApiFacadesModule {

    @Provides
    SemesterApi provideSemesterApi(Retrofit retrofit) {
        return new SemsterApiFacade(retrofit.create(SemesterApiKujon.class));
    }

    @Provides
    CoursesApi provideCoursesApi(Retrofit retrofit) {
        return new CoursesApiFacade(retrofit.create(CoursesApiKujon.class));
    }


    @Provides
    @GoogleDriveScope
    GetFilesFacade provideGetFilesFacade(Retrofit retrofit) {
        return new GetFilesFacade(retrofit.create(GetFilesKujon.class),retrofit.create(DeleteFileKujon.class));
    }
    @Provides
    @GoogleDriveScope
    GetFilesApi provideGetFiles(GetFilesFacade getFilesFacade) {
        return getFilesFacade;
    }

    @Provides
    ShareFileApi provideShareFile(Retrofit retrofit) {
        return new ShareFileFacade(retrofit.create(ShareFileKujon.class));
    }

    @Provides
    FileDownloadApi provideFileDownload(Retrofit retrofit) {
        return new FileDownloadApiFacade(retrofit.create(FileDownloadKujon.class));
    }

    @Provides
    DeleteFileApi provideDeleteFile(GetFilesFacade getFilesFacade) {
        return getFilesFacade;
    }

    @Provides
    FileUploadApi provideUploadFile(Retrofit retrofit, MultipartUtils multipartUtils, FileStreamUpdateMVP.Model model) {
        return new FileUploadApiFacade(retrofit.create(FileUploadKujon.class), multipartUtils, model);
    }

    @Provides
    CourseDetailsApi provideCourseDetailsApi(Retrofit retrofit) {
        return new CourseDetailsFacade(retrofit.create(CourseDetailsApiKujon.class));
    }


}
