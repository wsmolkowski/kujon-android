package mobi.kujon.google_drive.ui.fragments.files.dagger;

import dagger.Component;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.ui.activities.files.dagger.FilesActivityComponent;
import mobi.kujon.google_drive.ui.fragments.files.FilesListFragment;

/**
 *
 */

@ActivityScope
@Component(dependencies = FilesActivityComponent.class)
public interface FilesFragmentComponent {
    void inject(FilesListFragment filesListFragment);
}
