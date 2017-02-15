package mobi.kujon.google_drive.services.dowload_file.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.file_stream_update.FileCancelPresenter;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */
@Module
public class CancelPresenterModule {

    @ActivityScope
    @Provides
    FileStreamUpdateMVP.CancelPresenter provideCancelPresenter(FileStreamUpdateMVP.CancelModel cancelModel, SchedulersHolder schedulersHolder){
        return new FileCancelPresenter(cancelModel,schedulersHolder);
    }


}
