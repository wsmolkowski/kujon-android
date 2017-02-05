package mobi.kujon.google_drive.mvp.files_list;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.google_drive.model.dto.file.FileDtoFactory;
import mobi.kujon.google_drive.network.unwrapped_api.GetFiles;
import mobi.kujon.google_drive.utils.FilesFilter;
import mobi.kujon.utils.user_data.UserDataFacade;
import rx.Observable;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class FileListModelTest extends UnitTest {

    @Mock
    GetFiles getFiles;

    @Mock
    FilesFilter filesFilter;

    @Mock
    UserDataFacade userDataFacade;

    private FileListMVP.Model model;
    private String courseId;
    private String termId;
    private boolean reload;


    @Override
    protected void onSetup() {
        courseId = "course";
        termId = "term";
        model = new FileListModel(courseId, termId, getFiles, filesFilter, userDataFacade);
    }

    @Test
    public void returnProperValues() throws Exception {
        reload = true;
        List<KujonFile> filesFromBackend = provideKujonFiles(0, 10);
        Mockito.when(getFiles.getFiles(reload, courseId, termId)).thenReturn(Observable.just(filesFromBackend));
        List<KujonFile> filteredFilesAll = provideKujonFiles(11, 10);
        List<KujonFile> filteredFilesMy = provideKujonFiles(23, 10);
        Mockito.when(filesFilter.filterFiles(filesFromBackend,FilesOwnerType.ALL)).thenReturn(filteredFilesAll);
        Mockito.when(filesFilter.filterFiles(filesFromBackend,FilesOwnerType.MY)).thenReturn(filteredFilesMy);

        model.getFilesDto(reload,FilesOwnerType.ALL)
                .subscribe(fromModel ->{
                    assertEquals(fromModel, FileDtoFactory.createListOfDTOFiles(filteredFilesAll));
                });
        model.getFilesDto(reload,FilesOwnerType.MY)
                .subscribe(fromModel ->{
                    assertEquals(fromModel, FileDtoFactory.createListOfDTOFiles(filteredFilesMy));
                });
    }


    public static List<KujonFile> provideKujonFiles(int begin, int size) {
        List<KujonFile> kujonFiles = new ArrayList<>();
        for (int i = begin; i < begin + size; i++) {
            KujonFile kujonFile = new KujonFile();
            kujonFile.contentType = "content" + i;
            kujonFile.fileName = "name" + i;
            kujonFile.fileId = "id" + i;
            kujonFile.firstName = "firstName" + i;
            kujonFile.createdTime = new Date();
            kujonFile.fileSharedWith = new String[0];
            kujonFiles.add(kujonFile);
        }
        return kujonFiles;
    }
}