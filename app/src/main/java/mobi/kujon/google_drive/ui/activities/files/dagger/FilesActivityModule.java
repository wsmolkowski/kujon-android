package mobi.kujon.google_drive.ui.activities.files.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdatePresenter;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.files_list.FileListModel;
import mobi.kujon.google_drive.mvp.google_drive_api.GoogleDriveDowloadMVP;
import mobi.kujon.google_drive.mvp.google_drive_api.GoogleDriveDowloadModel;
import mobi.kujon.google_drive.network.unwrapped_api.GetFiles;
import mobi.kujon.google_drive.utils.FilesFilter;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */
@Module
public class FilesActivityModule {
    private String courseId,termId;

    public FilesActivityModule(String courseId, String termId) {
        this.courseId = courseId;
        this.termId = termId;
    }


    @ActivityScope
    @Provides
    FileListMVP.Model provideFileListModel(GetFiles getFiles){
        return new  FileListModel(courseId,termId,getFiles,new FilesFilter());
    }


    @ActivityScope
    @Provides
    GoogleDriveDowloadMVP.Model provideGoogleDriveModel(FileStreamUpdateMVP.Model model){
        return new GoogleDriveDowloadModel(null,model);
    }

    @ActivityScope
    @Provides
    FileStreamUpdateMVP.Presenter provideProgressPresenter(FileStreamUpdateMVP.Model model, SchedulersHolder schedulersHolder){
        return new FileStreamUpdatePresenter(model,schedulersHolder);
    }
}
