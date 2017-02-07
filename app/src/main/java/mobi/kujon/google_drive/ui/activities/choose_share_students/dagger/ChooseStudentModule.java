package mobi.kujon.google_drive.ui.activities.choose_share_students.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsModel;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsPresenter;
import mobi.kujon.google_drive.network.unwrapped_api.CourseDetailsApi;
import mobi.kujon.google_drive.utils.SchedulersHolder;

@Module
public class ChooseStudentModule {

    private ChooseStudentsMVP.View view;

    public ChooseStudentModule(ChooseStudentsMVP.View view) {
        this.view = view;
    }

    @Provides
    @ActivityScope
    ChooseStudentsMVP.Model provideChooseStudentsModel(CourseDetailsApi api) {
        return new ChooseStudentsModel(api);
    }

    @Provides
    @ActivityScope
    ChooseStudentsMVP.Presenter provideChooseStudentsPresenter(ChooseStudentsModel model, SchedulersHolder holder) {
        return new ChooseStudentsPresenter(model, view, holder);
    }
}
