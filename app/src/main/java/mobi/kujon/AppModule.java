package mobi.kujon;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    KujonApplication application;

    public AppModule(KujonApplication application) {
        this.application = application;
    }

    @Provides @Singleton KujonApplication providesKujonApplication() {
        return application;
    }

    @Provides @Singleton Application providesApplication() {
        return application;
    }

    @Provides @Singleton Context providesContext() {
        return application;
    }
}
