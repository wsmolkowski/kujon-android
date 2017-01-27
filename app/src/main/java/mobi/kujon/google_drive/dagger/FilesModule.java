package mobi.kujon.google_drive.dagger;

import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.KujonApplication;
import mobi.kujon.google_drive.dagger.injectors.InjectorProvider;
import mobi.kujon.google_drive.dagger.injectors.RuntimeInjectorProvider;
import mobi.kujon.google_drive.dagger.scopes.GoogleDriveScope;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateModel;
import mobi.kujon.google_drive.utils.MultipartUtils;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */


@Module
public class FilesModule {

    public FilesModule(KujonApplication application) {
        application.injectorProvider = new RuntimeInjectorProvider();

    }

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

    @Provides
    @GoogleDriveScope
    FileStreamUpdateMVP.Model provideFileStreamModel(){
        return new FileStreamUpdateModel();
    }



}
