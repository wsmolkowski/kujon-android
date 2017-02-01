package mobi.kujon.google_drive.services.upload.dagger;

import dagger.Component;
import mobi.kujon.google_drive.dagger.FilesComponent;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.services.upload.DowloadUploadFileServices;

/**
 *
 */

@ActivityScope
@Component(dependencies = FilesComponent.class,modules = DownloadUploadServiceModule.class)
public interface DownloadUploadServiceComponent {
    void inject(DowloadUploadFileServices dowloadUploadFileServices);
}
