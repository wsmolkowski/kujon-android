package mobi.kujon.google_drive.ui.activities.semesters.dagger;

import dagger.Component;
import mobi.kujon.google_drive.dagger.FilesComponent;

/**
 *
 */


@Component(dependencies = FilesComponent.class,modules = SemesterActivityModule.class)
public interface RuntimeSemesterActivityComponent extends SemesterActivityComponent{

}
