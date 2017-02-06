package mobi.kujon.google_drive.mvp.file_details;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.model.json.ShareFileTarget;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.model.json.SharedFile;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFileApi;
import rx.Observable;

import static org.junit.Assert.fail;

public class ShareFileModelTest extends UnitTest {

    @Mock
    ShareFileApi shareFileApi;

    private FileDetailsMVP.ShareFileModel model;
    private static final String FILE_ID = "1";
    private static final String STUDENT_ID = "15";

    @Override
    protected void onSetup() {
        model = new ShareFileModel(shareFileApi);
    }

    @Test
    public void shareFile() throws Exception {
        FileShareDto fileShareDto = new FileShareDto(FILE_ID, ShareFileTargetType.LIST, Collections.singletonList(STUDENT_ID));
        Mockito.when(shareFileApi.shareFile(new ShareFileTarget(fileShareDto)))
                .thenReturn(Observable.just(provideSharedFile()));

        model.shareFile(fileShareDto)
                .subscribe(sharedFile -> {
                    Assert.assertEquals(sharedFile.fileId, FILE_ID);
                    Assert.assertEquals(sharedFile.fileSharedWith.size(), 1);
                    Assert.assertEquals(sharedFile.fileSharedWith.get(0), STUDENT_ID);
                }, throwable -> fail());
    }


    private SharedFile provideSharedFile() {
        SharedFile file = new SharedFile();
        file.fileId = FILE_ID;
        file.fileSharedWith = Collections.singletonList(STUDENT_ID);
        return file;
    }
}