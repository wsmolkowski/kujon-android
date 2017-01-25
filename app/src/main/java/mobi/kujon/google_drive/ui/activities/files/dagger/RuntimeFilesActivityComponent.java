package mobi.kujon.google_drive.ui.activities.files.dagger;

import dagger.Component;
import mobi.kujon.google_drive.dagger.FilesComponent;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;

/**
 *
 */

@ActivityScope
@Component(dependencies = FilesComponent.class,modules = FilesActivityModule.class)
public interface RuntimeFilesActivityComponent extends FilesActivityComponent{
}
