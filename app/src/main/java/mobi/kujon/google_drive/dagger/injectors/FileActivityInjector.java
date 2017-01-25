package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.ui.activities.files.FilesActivity;
import mobi.kujon.google_drive.ui.activities.files.dagger.FilesActivityComponent;

/**
 *
 */

public class FileActivityInjector extends AbstractInjector<FilesActivity> {
    FilesActivityComponent filesActivityComponent;
    @Override
    public void inject(FilesActivity injectTo) {
    }

    public FilesActivityComponent getFilesActivityComponent() {
        return filesActivityComponent;
    }
}
