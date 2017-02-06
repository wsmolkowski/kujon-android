package mobi.kujon.google_drive.mvp.upload_file;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.model.json.UploadedFile;
import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.network.unwrapped_api.FileUpload;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 *
 */
public class UploadFilePresenterTest extends UnitTest{


    @Mock
    UploadFileMVP.View view;

    @Mock
    FileUpload fileUpload;

    private SchedulersHolder schedulersHolder= new SchedulersHolder(Schedulers.immediate(),Schedulers.immediate());

    private UploadFileMVP.Presenter presenter;
    @Override
    protected void onSetup() {
        presenter = new UploadFilePresenter(view,fileUpload,schedulersHolder);
    }


    @Test
    public void testUploadFile() throws Exception {
        FileUploadDto fileUploadDto = new FileUploadDto("courseId","termId", ShareFileTargetType.ALL,null);
        DataForFileUpload dataForFileUpload =new  DataForFileUpload("tekst".getBytes(),"image","title");
        List<UploadedFile> responseFromServer = Arrays.asList(new UploadedFile("a", "b", null));
        Mockito.when(fileUpload.uploadFile(fileUploadDto,dataForFileUpload))
                .thenReturn(Observable.just(responseFromServer));
        presenter.uploadFile(dataForFileUpload,fileUploadDto);
        Mockito.verify(view).onFileUploaded();
    }
}