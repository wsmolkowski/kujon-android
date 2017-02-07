package mobi.kujon.google_drive.ui.activities.file_details.dagger;


import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsFacade;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsMVP;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsPresenter;
import mobi.kujon.google_drive.mvp.file_details.ShareFilePresenter;
import mobi.kujon.google_drive.mvp.file_details.StudentsPresenter;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.utils.SchedulersHolder;

@Module
public class FileDetailsModule {

    private FileDetailsMVP.FileDetailsView fileDetailsView;
    private FileDetailsMVP.ShareView shareView;
    private FileDetailsMVP.StudentsView studentsView;
    private String courseId;
    private String termId;

    public FileDetailsModule(FileDetailsMVP.FileDetailsView fileDetailsView, FileDetailsMVP.ShareView shareView, FileDetailsMVP.StudentsView studentsView,
                             String courseId, String termId) {
        this.fileDetailsView = fileDetailsView;
        this.shareView = shareView;
        this.studentsView = studentsView;
        this.courseId = courseId;
        this.termId = termId;
    }

    @Provides
    @ActivityScope
    FileDetailsMVP.FileDetailsFacade provideFileDetailsFacade(ChooseStudentsMVP.Model studentModel, FileListMVP.Model filesModel) {
        return new FileDetailsFacade(studentModel, filesModel, courseId, termId);
    }

    @Provides
    @ActivityScope
    FileDetailsMVP.StudentsPresenter provideFileDetailsStudentsPresenter(FileDetailsMVP.FileDetailsFacade facade, SchedulersHolder schedulersHolder) {
        return new StudentsPresenter(facade, studentsView, schedulersHolder);
    }

    @Provides
    @ActivityScope
    FileDetailsMVP.ShareFilePresenter provideShareFilePresenter(FileDetailsMVP.ShareFileModel model, SchedulersHolder schedulersHolder) {
        return new ShareFilePresenter(model, shareView, schedulersHolder);
    }

    @Provides
    @ActivityScope
    FileDetailsMVP.FileDetailsPresenter provideFileDetailsPresenter(FileDetailsMVP.FileDetailsFacade facade, SchedulersHolder holder) {
        return new FileDetailsPresenter(facade, fileDetailsView, holder);
    }
}
