package mobi.kujon.google_drive.dagger;

import javax.inject.Singleton;

import dagger.Component;

/**
 *
 */

@Singleton
@Component(modules = {FilesModule.class, FilesApiFacadesModule.class})
public interface RuntimeFilesComponent extends FilesComponent {

}
