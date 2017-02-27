package mobi.kujon;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.utils.lang_force.ForceLanguage;
import mobi.kujon.utils.lang_force.ForceLanguageImpl;
import mobi.kujon.utils.shared_preferences.SharedPreferencesFacade;
import mobi.kujon.utils.shared_preferences.SharedPreferencesFacadeImpl;
import mobi.kujon.utils.user_data.UserDataFacade;
import mobi.kujon.utils.user_data.UserDataFacedImpl;

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

    @Provides @Singleton
    UserDataFacade provideUserDataFacade(SharedPreferencesFacade sharedPreferencesFacade) {
        return new UserDataFacedImpl(sharedPreferencesFacade);
    }


    @Provides
    @Singleton
    ForceLanguage provideForceLanguage(SharedPreferencesFacade sharedPreferencesFacade){
        return new ForceLanguageImpl(application,sharedPreferencesFacade);
    }
}
