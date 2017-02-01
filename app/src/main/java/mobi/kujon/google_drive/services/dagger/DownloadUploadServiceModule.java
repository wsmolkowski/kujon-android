package mobi.kujon.google_drive.services.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.google_drive_api.GoogleDriveDowloadMVP;
import mobi.kujon.google_drive.mvp.google_drive_api.GoogleDriveDowloadModel;
import mobi.kujon.google_drive.mvp.upload_file.UploadFileMVP;
import mobi.kujon.google_drive.mvp.upload_file.UploadFilePresenter;
import mobi.kujon.google_drive.network.unwrapped_api.FileUpload;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */
@Module
public class DownloadUploadServiceModule {
    private UploadFileMVP.View view;

    public DownloadUploadServiceModule(UploadFileMVP.View view) {
        this.view = view;
    }

    @Provides
    @ActivityScope
    public UploadFileMVP.Presenter provideUploadPresenter(SchedulersHolder schedulersHolder, FileUpload fileUpload){
        return new UploadFilePresenter(view,fileUpload,schedulersHolder);
    }

    @ActivityScope
    @Provides
    GoogleDriveDowloadMVP.Model provideGoogleDriveModel(FileStreamUpdateMVP.Model model){
        return new GoogleDriveDowloadModel(null,model);
    }
}
