package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.services.dowload_file.DowloadFileService;
import mobi.kujon.google_drive.services.dowload_file.dagger.CancelPresenterModule;
import mobi.kujon.google_drive.services.dowload_file.dagger.DaggerDowloadServiceComponent;

/**
 *
 */

public class DowloadServiceInjector extends AbstractInjector<DowloadFileService> {
    @Override
    public void inject(DowloadFileService injectTo) {
        DaggerDowloadServiceComponent.builder()
                .cancelPresenterModule(new CancelPresenterModule())
                .filesComponent(getFilesComponent(injectTo))
                .build().inject(injectTo);
    }
}
