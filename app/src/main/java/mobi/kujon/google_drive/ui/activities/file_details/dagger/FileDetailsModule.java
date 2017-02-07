package mobi.kujon.google_drive.ui.activities.file_details.dagger;


import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsMVP;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsPresenter;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsView;
import mobi.kujon.google_drive.mvp.file_details.ShareFilePresenter;
import mobi.kujon.google_drive.mvp.file_details.StudentsPresenter;
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
    FileDetailsMVP.StudentsPresenter provideFileDetailsStudentsPresenter(FileDetailsMVP.FileDetailsFacade facade, SchedulersHolder schedulersHolder) {
        return new StudentsPresenter(facade, fileDetailsView, schedulersHolder);
    }

    @Provides
    @ActivityScope
    FileDetailsMVP.ShareFilePresenter provideShareFilePresenter(FileDetailsMVP.ShareFileModel model, SchedulersHolder schedulersHolder) {
        return new ShareFilePresenter(model, fileDetailsView, schedulersHolder);
    }

    @Provides
    @ActivityScope
    FileDetailsMVP.FileDetailsPresenter provideFileDetailsPresenter(FileDetailsMVP.FileDetailsFacade facade, SchedulersHolder holder) {
        return new FileDetailsPresenter(facade, fileDetailsView, holder);
    }
}
