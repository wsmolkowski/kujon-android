package mobi.kujon.google_drive.dagger;

import android.app.Application;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.KujonApplication;
import mobi.kujon.google_drive.dagger.injectors.InjectorProvider;
import mobi.kujon.google_drive.dagger.injectors.RuntimeInjectorProvider;
import mobi.kujon.google_drive.utils.MultipartUtils;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */


@Module
public class FilesModule {

    private final KujonApplication application;

    public FilesModule(KujonApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    SchedulersHolder providesSchedulersHolder() {
        return new SchedulersHolder(AndroidSchedulers.mainThread(), Schedulers.from(Executors.newFixedThreadPool(4)));
    }

    @Provides
    @Singleton
    InjectorProvider provideInjectorProvider(){
        return new RuntimeInjectorProvider();
    }

    @Provides
    @Singleton
    MultipartUtils providesMultipartUtils() {
        return new MultipartUtils();
    }

    @Provides @Singleton KujonApplication providesKujonApplication() {
        return application;
    }

    @Provides @Singleton
    Application providesApplication() {
        return application;
    }

}
