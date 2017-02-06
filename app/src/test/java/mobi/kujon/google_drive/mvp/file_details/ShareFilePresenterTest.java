package mobi.kujon.google_drive.mvp.file_details;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import mobi.kujon.CommonExceptionCreator;
import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.model.json.SharedFile;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.schedulers.Schedulers;

public class ShareFilePresenterTest extends UnitTest {

    @Mock
    FileDetailsMVP.ShareFileModel shareFileModel;

    @Mock
    FileDetailsMVP.ShareView view;

    private FileDetailsMVP.ShareFilePresenter presenter;
    private static final String FILE_ID = "1";
    private static final String STUDENT_ID = "15";

    @Override
    protected void onSetup() {
        presenter = new ShareFilePresenter(shareFileModel, view, new SchedulersHolder(Schedulers.immediate(), Schedulers.immediate()));
    }

    @Test
    public void shareFileWithCorrectData() throws Exception {
        Mockito.when(shareFileModel.shareFile( new FileShareDto(FILE_ID, ShareFileTargetType.LIST, Collections.singletonList(STUDENT_ID))))
                .thenReturn(Observable.just(provideSharedFile()));
        presenter.shareFileWith(FILE_ID, ShareFileTargetType.LIST, provideStudentDtos());
        Mockito.verify(view).fileShared();
    }

    @Test
    public void shareFileWithBadData() throws Exception {
        NullPointerException nullPointerException = CommonExceptionCreator.provideNullPointer(" Jakis blad");
        Mockito.when(shareFileModel.shareFile( new FileShareDto(FILE_ID, ShareFileTargetType.LIST, Collections.singletonList(STUDENT_ID))))
                .thenReturn(Observable.error(nullPointerException));
        presenter.shareFileWith(FILE_ID, ShareFileTargetType.LIST, provideStudentDtos());
        Mockito.verify(view).handleException(nullPointerException);
    }

    private SharedFile provideSharedFile() {
        SharedFile file = new SharedFile();
        file.fileId = FILE_ID;
        file.fileSharedWith = Collections.singletonList(STUDENT_ID);
        return file;
    }

    private List<DisableableStudentShareDTO> provideStudentDtos() {
        return Collections.singletonList(new DisableableStudentShareDTO("John", STUDENT_ID, true, true));
    }
}