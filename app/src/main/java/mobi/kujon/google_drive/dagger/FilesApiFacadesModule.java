package mobi.kujon.google_drive.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.NetModule;
import mobi.kujon.google_drive.network.api.CoursesApiKujon;
import mobi.kujon.google_drive.network.api.DeleteFileKujon;
import mobi.kujon.google_drive.network.api.FileDownloadKujon;
import mobi.kujon.google_drive.network.api.FileUploadKujon;
import mobi.kujon.google_drive.network.api.GetFilesKujon;
import mobi.kujon.google_drive.network.api.SemesterApiKujon;
import mobi.kujon.google_drive.network.api.ShareFileKujon;
import mobi.kujon.google_drive.network.facade.CoursesApiFacade;
import mobi.kujon.google_drive.network.facade.DeleteFileFacade;
import mobi.kujon.google_drive.network.facade.FileDownloadFacade;
import mobi.kujon.google_drive.network.facade.FileUploadFacade;
import mobi.kujon.google_drive.network.facade.GetFilesFacade;
import mobi.kujon.google_drive.network.facade.SemsterApiFacade;
import mobi.kujon.google_drive.network.facade.ShareFileFacade;
import mobi.kujon.google_drive.network.unwrapped_api.CoursesApi;
import mobi.kujon.google_drive.network.unwrapped_api.DeleteFile;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownload;
import mobi.kujon.google_drive.network.unwrapped_api.FileUpload;
import mobi.kujon.google_drive.network.unwrapped_api.GetFiles;
import mobi.kujon.google_drive.network.unwrapped_api.SemesterApi;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFile;
import mobi.kujon.google_drive.utils.MultipartUtils;
import retrofit2.Retrofit;

/**
 *
 */


@Module(includes = NetModule.class)
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
    GetFiles provideGetFiles(Retrofit retrofit) {
        return new GetFilesFacade(retrofit.create(GetFilesKujon.class));
    }

    @Provides
    ShareFile provideShareFile(Retrofit retrofit) {
        return new ShareFileFacade(retrofit.create(ShareFileKujon.class));
    }

    @Provides
    FileDownload provideFileDownload(Retrofit retrofit) {
        return new FileDownloadFacade(retrofit.create(FileDownloadKujon.class));
    }

    @Provides
    DeleteFile provideDeleteFile(Retrofit retrofit) {
        return new DeleteFileFacade(retrofit.create(DeleteFileKujon.class));
    }

    @Provides
    FileUpload provideUploadFile(Retrofit retrofit, MultipartUtils multipartUtils) {
        return new FileUploadFacade(retrofit.create(FileUploadKujon.class), multipartUtils);
    }



}
