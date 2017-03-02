package mobi.kujon.google_drive.ui.fragments.files.dagger;

import dagger.Module;
import dagger.Provides;
import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.files_list.FilesListPresenter;
import mobi.kujon.google_drive.utils.FilesFilter;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */
@Module
public class FilesFragmentModule {

    private FileListMVP.FilesView filesView;


    public FilesFragmentModule(FileListMVP.FilesView filesView) {
        this.filesView = filesView;
    }

    @ActivityScope
    @Provides
    public FileListMVP.LoadPresenter providePresenter(FileListMVP.Model model, SchedulersHolder schedulersHolder){
        return new FilesListPresenter(model,filesView,schedulersHolder,new FilesFilter());
    }
}
