package mobi.kujon.google_drive.dagger;

import dagger.Component;
import mobi.kujon.KujonComponent;
import mobi.kujon.google_drive.dagger.scopes.GoogleDriveScope;

/**
 *
 */

@GoogleDriveScope
@Component(modules = {FilesModule.class, FilesApiFacadesModule.class},dependencies = KujonComponent.class)
public interface RuntimeFilesComponent extends FilesComponent {

}
