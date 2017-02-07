package mobi.kujon.google_drive.ui.activities.file_details.dagger;


import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsModel;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsModel;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsMVP;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsPresenter;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsView;
import mobi.kujon.google_drive.mvp.file_details.ShareFileModel;
import mobi.kujon.google_drive.mvp.file_details.ShareFilePresenter;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFileApi;
import mobi.kujon.google_drive.utils.SchedulersHolder;

@Module
public class FileDetailsModule {

    private FileDetailsView fileDetailsView;
    private String courseId;
    private String termId;

    public FileDetailsModule(FileDetailsView view, String courseId, String termId) {
        this.fileDetailsView = view;
        this.courseId = courseId;
        this.termId = termId;
    }

    @Provides
    @ActivityScope
    FileDetailsMVP.FileDetailsModel provideFileDetailsFacade(ChooseStudentsModel chooseStudentModel, GetFilesApi getFilesApi) {
        return new FileDetailsModel(chooseStudentModel, getFilesApi, courseId, termId);
    }

    @Provides
    @ActivityScope
    FileDetailsMVP.ShareFileModel provideShareFileModel(ShareFileApi shareFileApi) {
        return new ShareFileModel(shareFileApi);
    }


    @Provides
    @ActivityScope
    FileDetailsMVP.ShareFilePresenter provideShareFilePresenter(FileDetailsMVP.ShareFileModel model, SchedulersHolder schedulersHolder) {
        return new ShareFilePresenter(model, fileDetailsView, schedulersHolder);
    }

    @Provides
    @ActivityScope
    FileDetailsMVP.FileDetailsPresenter provideFileDetailsPresenter(FileDetailsMVP.FileDetailsModel facade, SchedulersHolder holder) {
        return new FileDetailsPresenter(facade, fileDetailsView, holder);
    }
}
