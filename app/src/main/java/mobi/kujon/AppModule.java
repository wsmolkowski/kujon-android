package mobi.kujon;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.utils.shared_preferences.SharedPreferencesFacade;
import mobi.kujon.utils.shared_preferences.SharedPreferencesFacadeImpl;

@Module
public class AppModule {

    private static final String PREFERENCES_NAME = "kujon_prefs_name";
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

    @Provides @Singleton
    SharedPreferencesFacade proviceSharedPreferencesFacade(KujonApplication kujonApplication){
        return new SharedPreferencesFacadeImpl(application.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE));
    }

}
