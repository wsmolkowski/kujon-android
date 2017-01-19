package mobi.kujon.google_drive.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.NetModule;
import mobi.kujon.google_drive.network.FileDownload;
import mobi.kujon.google_drive.network.FileDownloadKujon;
import mobi.kujon.google_drive.network.GetFiles;
import mobi.kujon.google_drive.network.GetFilesKujon;
import mobi.kujon.google_drive.network.SemesterApi;
import mobi.kujon.google_drive.network.SemesterApiKujon;
import mobi.kujon.google_drive.network.ShareFile;
import mobi.kujon.google_drive.network.ShareFileKujon;
import mobi.kujon.google_drive.network.facade.FileDownloadFacade;
import mobi.kujon.google_drive.network.facade.GetFilesFacade;
import mobi.kujon.google_drive.network.facade.SemsterApiFacade;
import mobi.kujon.google_drive.network.facade.ShareFileFacade;
import retrofit2.Retrofit;

/**
 *
 */


@Module(includes = NetModule.class)
public class FilesApiFacadesModule {

    @Provides
    SemesterApi provideSemseterApi(Retrofit retrofit) {
        return new SemsterApiFacade(retrofit.create(SemesterApiKujon.class));
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

}
