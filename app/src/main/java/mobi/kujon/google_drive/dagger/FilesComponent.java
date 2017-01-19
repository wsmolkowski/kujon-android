package mobi.kujon.google_drive.dagger;

import dagger.Component;

/**
 *
 */


@Component(modules = {FilesModule.class, FilesApiFacadesModule.class})
public interface FilesComponent {
}
