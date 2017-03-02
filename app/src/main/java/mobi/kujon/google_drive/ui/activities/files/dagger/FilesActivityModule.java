package mobi.kujon.google_drive.ui.activities.files.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.file_stream_update.FileCancelPresenter;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdatePresenter;
import mobi.kujon.google_drive.mvp.files_list.DeleteFileModel;
import mobi.kujon.google_drive.mvp.files_list.DeletePresenter;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.files_list.FileListModel;
import mobi.kujon.google_drive.mvp.upload_file.UploadFileMVP;
import mobi.kujon.google_drive.mvp.upload_file.UploadFilePresenter;
import mobi.kujon.google_drive.network.unwrapped_api.DeleteFileApi;
import mobi.kujon.google_drive.network.unwrapped_api.FileUploadApi;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import mobi.kujon.google_drive.ui.activities.files.FileActivityView;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */
@Module
public class FilesActivityModule {
    private String courseId, termId;
    private FileActivityView deleteView;

    public FilesActivityModule(String courseId, String termId, FileActivityView deleteView) {
        this.courseId = courseId;
        this.termId = termId;
        this.deleteView = deleteView;
    }


    @ActivityScope
    @Provides
    FileListMVP.Model provideFileListModel(GetFilesApi getFilesApi, SchedulersHolder schedulersHolder) {
        return new FileListModel(courseId, termId, getFilesApi, schedulersHolder);
    }

    @ActivityScope
    @Provides
    FileListMVP.DeleteModel provideFileDeleteModel(DeleteFileApi deleteFileApi) {
        return new DeleteFileModel(deleteFileApi,courseId,termId);
    }


    @ActivityScope
    @Provides
    FileStreamUpdateMVP.Presenter provideProgressPresenter(FileStreamUpdateMVP.Model model, SchedulersHolder schedulersHolder) {
        return new FileStreamUpdatePresenter(model, schedulersHolder);
    }


    @ActivityScope
    @Provides
    FileListMVP.DeletePresenter provideDeletePresenter(FileListMVP.DeleteModel deleteFileModel, SchedulersHolder schedulersHolder) {
        return new DeletePresenter(deleteFileModel, deleteView, schedulersHolder);
    }

    @Provides
    @ActivityScope
    public UploadFileMVP.Presenter provideUploadPresenter(SchedulersHolder schedulersHolder, FileUploadApi fileUploadApi, FileStreamUpdateMVP.Model model){
        return new UploadFilePresenter(deleteView, fileUploadApi,schedulersHolder,model);
    }

    @ActivityScope
    @Provides
    FileStreamUpdateMVP.CancelPresenter provideCancelPresenter(FileStreamUpdateMVP.CancelModel cancelModel,SchedulersHolder schedulersHolder){
        return new FileCancelPresenter(cancelModel,schedulersHolder);
    }
}
