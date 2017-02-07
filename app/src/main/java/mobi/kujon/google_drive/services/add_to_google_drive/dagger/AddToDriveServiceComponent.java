package mobi.kujon.google_drive.services.add_to_google_drive.dagger;

import dagger.Component;
import mobi.kujon.google_drive.dagger.FilesComponent;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.services.add_to_google_drive.AddToGoogleDriveService;

/**
 *
 */


@ActivityScope
@Component(modules = AddToDriveModule.class, dependencies = FilesComponent.class)
public interface AddToDriveServiceComponent {
    void inject(AddToGoogleDriveService addToGoogleDriveService);
}
