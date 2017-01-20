package mobi.kujon.google_drive.ui.activities.semesters.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.semester_list.SemestersMVP;
import mobi.kujon.google_drive.mvp.semester_list.SemestersModel;
import mobi.kujon.google_drive.mvp.semester_list.SemestersPresenter;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */
@Module
public class SemesterActivityModule {
    private SemestersMVP.View view;

    public SemesterActivityModule(SemestersMVP.View view) {
        this.view = view;
    }

    @Provides
    @ActivityScope
    SemestersMVP.Presenter provideSemesterPresenter(SemestersModel semestersModel, SchedulersHolder schedulersHolder){
        return new SemestersPresenter(view,semestersModel,schedulersHolder);
    }
}
