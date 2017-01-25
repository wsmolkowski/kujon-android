package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.ui.activities.files.dagger.FilesActivityComponent;
import mobi.kujon.google_drive.ui.fragments.files.FilesListFragment;
import mobi.kujon.google_drive.ui.fragments.files.dagger.DaggerFilesFragmentComponent;
import mobi.kujon.google_drive.ui.fragments.files.dagger.FilesFragmentModule;

/**
 *
 */

public class FilesListFragmentInjector extends AbstractInjector<FilesListFragment> {
    private FilesActivityComponent filesActivityComponent;

    public FilesListFragmentInjector(FilesActivityComponent filesActivityComponent) {
        this.filesActivityComponent = filesActivityComponent;
    }

    @Override
    public void inject(FilesListFragment injectTo) {
        DaggerFilesFragmentComponent.builder()
                .filesActivityComponent(filesActivityComponent)
                .filesFragmentModule(new FilesFragmentModule(injectTo))
                .build().inject(injectTo);
    }
}
