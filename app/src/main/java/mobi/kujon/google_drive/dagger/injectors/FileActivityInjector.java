package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.ui.activities.files.FilesActivity;
import mobi.kujon.google_drive.ui.activities.files.dagger.DaggerRuntimeFilesActivityComponent;
import mobi.kujon.google_drive.ui.activities.files.dagger.FilesActivityComponent;
import mobi.kujon.google_drive.ui.activities.files.dagger.FilesActivityModule;

/**
 *
 */

public class FileActivityInjector extends AbstractInjector<FilesActivity> {
    FilesActivityComponent filesActivityComponent;
    @Override
    public void inject(FilesActivity injectTo) {
        filesActivityComponent =  DaggerRuntimeFilesActivityComponent.builder()
                .filesActivityModule(new FilesActivityModule(injectTo.getCoursId(),injectTo.getTermId(),injectTo))
                .filesComponent(getFilesComponent(injectTo))
                .build();
        filesActivityComponent.inject(injectTo);
    }

    public FilesActivityComponent getFilesActivityComponent() {
        return filesActivityComponent;
    }
}
