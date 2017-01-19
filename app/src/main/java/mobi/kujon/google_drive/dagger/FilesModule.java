package mobi.kujon.google_drive.dagger;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */


@Module
public class FilesModule {



    @Provides
    @Singleton
    SchedulersHolder providesSchedulersHolder(){
      return   new SchedulersHolder(AndroidSchedulers.mainThread(),Schedulers.from(Executors.newFixedThreadPool(4)));
    }


}
