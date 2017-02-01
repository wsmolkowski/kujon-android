package mobi.kujon.google_drive.dagger.injectors;


import mobi.kujon.google_drive.ui.activities.choose_share_students.ChooseStudentActivity;
import mobi.kujon.google_drive.ui.activities.choose_share_students.dagger.ChooseStudentModule;
import mobi.kujon.google_drive.ui.activities.choose_share_students.dagger.DaggerRuntimeChooseStudentActivityComponent;

public class ChooseStudentInjector extends AbstractInjector<ChooseStudentActivity> {

    @Override
    public void inject(ChooseStudentActivity injectTo) {
        DaggerRuntimeChooseStudentActivityComponent.builder()
                .filesComponent(this.getFilesComponent(injectTo))
                .chooseStudentModule(new ChooseStudentModule(injectTo))
                .build().inject(injectTo);
    }
}
