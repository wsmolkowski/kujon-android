package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.ui.activities.semesters.SemestersActivity;
import mobi.kujon.google_drive.ui.activities.semesters.dagger.DaggerRuntimeSemesterActivityComponent;
import mobi.kujon.google_drive.ui.activities.semesters.dagger.SemesterActivityModule;

/**
 *
 */

public class SemesterActivityInjector extends AbtractInjector<SemestersActivity> {
    @Override
    public void inject(SemestersActivity injectTo) {
        DaggerRuntimeSemesterActivityComponent.builder()
                .filesComponent(this.getFilesComponent(injectTo))
                .semesterActivityModule(new SemesterActivityModule(injectTo))
                .build().inject(injectTo);
    }
}
