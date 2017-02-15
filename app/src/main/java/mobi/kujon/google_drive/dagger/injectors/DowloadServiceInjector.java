package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.services.dowload_file.DowloadFileService;
import mobi.kujon.google_drive.ui.activities.files.dagger.DaggerRuntimeFilesActivityComponent;

/**
 *
 */

public class DowloadServiceInjector extends AbstractInjector<DowloadFileService> {
    @Override
    public void inject(DowloadFileService injectTo) {
        DaggerRuntimeFilesActivityComponent.builder()
                .filesComponent(getFilesComponent(injectTo))
                .build().inject(injectTo);
    }
}
