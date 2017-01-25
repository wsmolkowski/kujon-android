package mobi.kujon.google_drive.dagger;

import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.injectors.InjectorProvider;
import mobi.kujon.google_drive.dagger.injectors.RuntimeInjectorProvider;
import mobi.kujon.google_drive.dagger.scopes.GoogleDriveScope;
import mobi.kujon.google_drive.utils.MultipartUtils;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */


@Module
public class FilesModule {


    @Provides
    @GoogleDriveScope
    SchedulersHolder providesSchedulersHolder() {
        return new SchedulersHolder(AndroidSchedulers.mainThread(), Schedulers.from(Executors.newFixedThreadPool(4)));
    }

    @Provides
    @GoogleDriveScope
    InjectorProvider provideInjectorProvider(){
        return new RuntimeInjectorProvider();
    }

    @Provides
    @GoogleDriveScope
    MultipartUtils providesMultipartUtils() {
        return new MultipartUtils();
    }



}
