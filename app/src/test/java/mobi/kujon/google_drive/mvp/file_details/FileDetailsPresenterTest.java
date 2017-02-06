package mobi.kujon.google_drive.mvp.file_details;


import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import mobi.kujon.CommonExceptionCreator;
import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.json.KujonFile;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.model.json.SharedFile;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.NormalFileType;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.schedulers.Schedulers;

public class FileDetailsPresenterTest extends UnitTest {

    @Mock
    FileDetailsFacade fileDetailsFacade;

    @Mock
    FileDetailsMVP.View view;

    private FileDetailsMVP.Presenter presenter;

    private static final String FILE_ID = "1";
    private static final String STUDENT_ID = "1";

    @Override
    protected void onSetup() {
        presenter = new FileDetailsPresenter(fileDetailsFacade, view,
                new SchedulersHolder(Schedulers.immediate(), Schedulers.immediate()));
    }

    @Test
    public void testShareFileCorrectData() throws Exception {
        List<DisableableStudentShareDTO> dtos = provideStudentDTOs();
        Mockito.when(fileDetailsFacade.shareFile(provideFileShareDto()))
                .thenReturn(Observable.just(provideSharedFile()));
        presenter.shareFileWith(FILE_ID, dtos);
        Mockito.verify(view).fileShared();
    }

    @Test
    public void testShareFileBadData() throws Exception {
        List<DisableableStudentShareDTO> dtos = provideStudentDTOs();
        NullPointerException nullPointerException = CommonExceptionCreator.provideNullPointer(" Jakis blad");
        Mockito.when(fileDetailsFacade.shareFile(provideFileShareDto()))
                .thenReturn(Observable.error(nullPointerException));
        presenter.shareFileWith(FILE_ID, dtos);
        Mockito.verify(view).handleException(nullPointerException);
    }

    @Test
    public void testShareWithEveryoneChooseEveryone() throws Exception {
        List<DisableableStudentShareDTO> dtos = provideStudentDTOs();
        dtos.get(0).setEnabled(false);
        presenter.chooseEveryoneToShare(true, dtos);
        Mockito.verify(view).displayFileShares(dtos);
        Assert.assertFalse(dtos.get(0).isEnabled());
    }

    @Test
    public void testShareWithEveryoneChooseManually() throws Exception {
        List<DisableableStudentShareDTO> dtos = provideStudentDTOs();
        presenter.chooseEveryoneToShare(false, dtos);
        Mockito.verify(view).displayFileShares(dtos);
        Assert.assertTrue(dtos.get(0).isEnabled());
    }

    @Test
    public void testLoadFileDetailsCorrectData() throws Exception {
        FileDTO fileDTO = provideFileDTO();
        List<DisableableStudentShareDTO> studentShareDTOs = provideStudentDTOs();
        Mockito.when(fileDetailsFacade.loadFileProperties(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(Observable.just(fileDTO));
        Mockito.when(fileDetailsFacade.loadStudentShares(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(Observable.just(studentShareDTOs));
        presenter.loadFileDetails(FILE_ID, true);
        Mockito.verify(view).displayFileShares(studentShareDTOs);
        Mockito.verify(view).displayFileProperties(fileDTO);
    }

    @Test
    public void testLoadFileDetailsBadData() throws Exception {
        NullPointerException nullPointerException = CommonExceptionCreator.provideNullPointer(" Jakis blad");
        Mockito.when(fileDetailsFacade.loadFileProperties(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(Observable.error(nullPointerException));
        Mockito.when(fileDetailsFacade.loadStudentShares(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(Observable.error(nullPointerException));
        presenter.loadFileDetails(FILE_ID, true);
        Mockito.verify(view, Mockito.times(2)).handleException(nullPointerException);
    }

    public static List<DisableableStudentShareDTO> provideStudentDTOs() {
        return Collections.singletonList(
                new DisableableStudentShareDTO(new StudentShareDto("John Wick", STUDENT_ID, true), false));
    }

    public static FileShareDto provideFileShareDto() {
        return new FileShareDto(FILE_ID, ShareFileTargetType.ALL, Collections.singletonList(STUDENT_ID));
    }

    public static SharedFile provideSharedFile() {
        SharedFile sharedFile = new SharedFile();
        sharedFile.fileSharedWith = Collections.singletonList(STUDENT_ID);
        sharedFile.fileId = FILE_ID;
        return sharedFile;
    }

    public static FileDTO provideFileDTO() {
        KujonFile kujonFile = new KujonFile();
        kujonFile.contentType = "content";
        kujonFile.fileName = "name";
        kujonFile.fileId = "id";
        kujonFile.firstName = "firstName";
        kujonFile.createdTime = new Date();
        kujonFile.fileSharedWith = new String[1];
        kujonFile.fileSharedWith[0] = STUDENT_ID;
        return new NormalFileType(kujonFile);
    }


}
