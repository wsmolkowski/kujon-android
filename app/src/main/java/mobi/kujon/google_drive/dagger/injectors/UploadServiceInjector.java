package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.services.upload.DowloadUploadFileServices;
import mobi.kujon.google_drive.services.upload.dagger.DaggerDownloadUploadServiceComponent;
import mobi.kujon.google_drive.services.upload.dagger.DownloadUploadServiceModule;

/**
 *
 */

public class UploadServiceInjector extends AbstractInjector<DowloadUploadFileServices> {
    @Override
    public void inject(DowloadUploadFileServices injectTo) {
        DaggerDownloadUploadServiceComponent.builder()
                .downloadUploadServiceModule(new DownloadUploadServiceModule(injectTo))
                .filesComponent(this.getFilesComponent(injectTo))
                .build().inject(injectTo);
    }
}
