package mobi.kujon.google_drive.dagger.injectors;

import android.content.Context;

import mobi.kujon.KujonApplication;
import mobi.kujon.google_drive.dagger.FilesComponent;

/**
 *
 */

public abstract class AbstractInjector<T> implements Injector<T> {

    protected FilesComponent getFilesComponent(Context activity){
        return ((KujonApplication)activity.getApplicationContext()).getFilesComponent();
    }
}
