package mobi.kujon.google_drive.services.add_to_google_drive.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.KujonApplication;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.file_stream_update.FileCancelPresenter;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.upload_to_drive.UploadToDrive;
import mobi.kujon.google_drive.mvp.upload_to_drive.UploadToDrivePresenter;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownloadApi;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import mobi.kujon.google_drive.utils.TempFileCreator;

/**
 *
 */
@Module
public class AddToDriveModule  {


    private UploadToDrive.View view;

    public AddToDriveModule(UploadToDrive.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UploadToDrive.Presenter providePresenter(FileDownloadApi fileDownloadApi, SchedulersHolder schedulersHolder, KujonApplication kujonApplication, FileStreamUpdateMVP.Model cancelModel){
        return new UploadToDrivePresenter(view,fileDownloadApi,schedulersHolder,new TempFileCreator(kujonApplication),cancelModel);
    }

    @ActivityScope
    @Provides
    FileStreamUpdateMVP.CancelPresenter provideCancelPresenter(FileStreamUpdateMVP.CancelModel cancelModel,SchedulersHolder schedulersHolder){
        return new FileCancelPresenter(cancelModel,schedulersHolder);
    }


}
