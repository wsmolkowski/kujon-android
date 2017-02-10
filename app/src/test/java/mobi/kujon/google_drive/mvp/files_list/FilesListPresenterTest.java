package mobi.kujon.google_drive.mvp.files_list;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.FileDtoFactory;
import mobi.kujon.google_drive.ui.dialogs.sort_strategy.DateSortStrategy;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.times;

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
        presenter = new FilesListPresenter(model,filesView,schedulersHolder);
    }

    @Test
    public void testCorrectPath() throws Exception {
        List<FileDTO> filesDtoFromModel = FileDtoFactory.createListOfDTOFiles(FileListModelTest.provideKujonFiles(0, 10));
        Mockito.when(model.getFilesDto(Mockito.anyBoolean(),Mockito.anyInt())).thenReturn(Observable.just(filesDtoFromModel));

        presenter.loadListOfFiles(true,FilesOwnerType.ALL, new DateSortStrategy());
        Mockito.verify(filesView).listOfFilesLoaded(filesDtoFromModel);
    }
    @Test
    public void testException() throws Exception {

        NullPointerException errorFromModel = new NullPointerException("to jest bład ");
        Mockito.when(model.getFilesDto(Mockito.anyBoolean(),Mockito.anyInt())).thenReturn(
                Observable.error(errorFromModel)
        );

        presenter.loadListOfFiles(true,FilesOwnerType.ALL, new DateSortStrategy());
        Mockito.verify(filesView).handleException(errorFromModel);
    }

    @Test
    public void testNoFileException() throws Exception {


        Mockito.when(model.getFilesDto(Mockito.anyBoolean(),Mockito.anyInt())).thenReturn(
                Observable.error(new NoFileException())
        );

        presenter.loadListOfFiles(true,FilesOwnerType.ALL, new DateSortStrategy());
        Mockito.verify(filesView).noFilesAdded();
    }

    @Test
    public void testClearCalls() throws Exception {
        PublishSubject<List<FileDTO>> subject = PublishSubject.create();
        Mockito.when(model.getFilesDto(Mockito.anyBoolean(),Mockito.anyInt())).thenReturn(
              subject
        );
        List<FileDTO> filesDtoFromModel = FileDtoFactory.createListOfDTOFiles(FileListModelTest.provideKujonFiles(0, 10));
        presenter.loadListOfFiles(true,FilesOwnerType.ALL, new DateSortStrategy());
        subject.onNext(filesDtoFromModel);
        subject.onNext(filesDtoFromModel);
        Mockito.verify(filesView,times(2)).listOfFilesLoaded(filesDtoFromModel);
        presenter.clearSubscriptions();
        subject.onNext(filesDtoFromModel);
        subject.onNext(filesDtoFromModel);
        Mockito.verify(filesView,times(2)).listOfFilesLoaded(filesDtoFromModel);
    }
}