package mobi.kujon.google_drive.dagger.injectors;

import mobi.kujon.google_drive.services.add_to_google_drive.AddToGoogleDriveService;
import mobi.kujon.google_drive.services.add_to_google_drive.dagger.AddToDriveModule;
import mobi.kujon.google_drive.services.add_to_google_drive.dagger.DaggerAddToDriveServiceComponent;

/**
 *
 */

public class UploadToDriveServiceInjector extends AbstractInjector<AddToGoogleDriveService> {
    @Override
    public void inject(AddToGoogleDriveService injectTo) {
        DaggerAddToDriveServiceComponent.builder()
                .filesComponent(this.getFilesComponent(injectTo))
                .addToDriveModule(new AddToDriveModule(injectTo))
                .build().inject(injectTo);
    }
}
