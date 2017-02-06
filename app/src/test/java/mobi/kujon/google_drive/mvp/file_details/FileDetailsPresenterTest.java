package mobi.kujon.google_drive.mvp.file_details;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Date;

import mobi.kujon.CommonExceptionCreator;
import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.NormalFileType;
import mobi.kujon.google_drive.model.json.KujonFile;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.schedulers.Schedulers;

public class FileDetailsPresenterTest extends UnitTest {

    @Mock
    FileDetailsFacade fileDetailsFacade;

    @Mock
    FileDetailsMVP.FileDetailsView view;

    private FileDetailsMVP.FileDetailsPresenter fileDetailsPresenter;

    private static final String FILE_ID = "1";

    @Override
    protected void onSetup() {
        fileDetailsPresenter = new FileDetailsPresenter(fileDetailsFacade, view, new SchedulersHolder(Schedulers.immediate(), Schedulers.immediate()));
    }

    @Test
    public void loadFileDetailsFileCorrect() throws Exception {
        Mockito.when(fileDetailsFacade.loadFileDetails(FILE_ID, true))
                .thenReturn(Observable.just(provideFileDTO()));
        fileDetailsPresenter.loadFileDetails(FILE_ID, true);
        Mockito.verify(view).displayFileProperties(provideFileDTO());
    }

    @Test
    public void loadFileDetailsError() throws Exception {
        NullPointerException nullPointerException = CommonExceptionCreator.provideNullPointer(" Jakis blad");
        Mockito.when(fileDetailsFacade.loadFileDetails(FILE_ID, true))
                .thenReturn(Observable.error(nullPointerException));
        fileDetailsPresenter.loadFileDetails(FILE_ID, true);
        Mockito.verify(view).handleException(nullPointerException);
    }

    private FileDTO provideFileDTO() {
        KujonFile kujonFile = new KujonFile();
        kujonFile.contentType = "content";
        kujonFile.shareType = ShareFileTargetType.LIST;
        kujonFile.fileName = "name";
        kujonFile.fileId = FILE_ID;
        kujonFile.firstName = "firstName";
        kujonFile.createdTime = new Date();
        kujonFile.fileSharedWith = new String[]{"sid"};
        return new NormalFileType(kujonFile);
    }
}