package mobi.kujon.google_drive.services.dowload_file.dagger;

import dagger.Component;
import mobi.kujon.google_drive.dagger.FilesComponent;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.services.dowload_file.DowloadFileService;

/**
 *
 */


@ActivityScope
@Component(modules = CancelPresenterModule.class,dependencies = FilesComponent.class)
public interface DowloadServiceComponent {
    void inject(DowloadFileService dowloadFileService);
}
