package mobi.kujon.google_drive.mvp.files_list;

import org.mockito.Mock;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.schedulers.Schedulers;

/**
 *
 */
public class FilesListPresenterTest extends UnitTest{


    @Mock
    FileListMVP.Model model;
    @Mock
    FileListMVP.FilesView filesView;

    private SchedulersHolder schedulersHolder = new SchedulersHolder(Schedulers.immediate(), Schedulers.immediate());
    private FileListMVP.LoadPresenter presenter;

    @Override
    protected void onSetup() {

    }
}