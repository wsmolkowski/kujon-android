package mobi.kujon.google_drive.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.NetModule;
import mobi.kujon.google_drive.network.SemesterApi;
import mobi.kujon.google_drive.network.SemesterApiKujon;
import mobi.kujon.google_drive.network.facade.SemsterApiFacade;
import retrofit2.Retrofit;

/**
 *
 */


@Module(includes = NetModule.class)
public class FilesApiFacadesModule {

    @Provides
    SemesterApi provideSemseterApi(Retrofit retrofit){
        return new SemsterApiFacade(retrofit.create(SemesterApiKujon.class));
    }



}
