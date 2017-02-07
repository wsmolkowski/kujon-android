package mobi.kujon.google_drive.ui.activities.file_details.dagger;

import dagger.Component;
import mobi.kujon.google_drive.dagger.FilesComponent;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.ui.activities.files.dagger.FilesActivityModule;

@ActivityScope
@Component(modules = {FileDetailsModule.class, FilesActivityModule.class}, dependencies = FilesComponent.class)
public interface RuntimeFileDetailsActivityComponent extends FileDetailsActivityComponent {
}
