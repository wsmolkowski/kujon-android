package mobi.kujon.google_drive.ui.activities.choose_share_students.dagger;

import dagger.Component;
import mobi.kujon.google_drive.dagger.FilesComponent;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;

@ActivityScope
@Component(dependencies = FilesComponent.class, modules = ChooseStudentModule.class)
public interface RuntimeChooseStudentActivityComponent extends ChooseStudentActivityComponent {
}
