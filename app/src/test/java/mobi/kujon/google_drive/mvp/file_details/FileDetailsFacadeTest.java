package mobi.kujon.google_drive.mvp.file_details;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.NormalFileType;
import mobi.kujon.google_drive.model.json.KujonFile;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.files_list.FilesOwnerType;
import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileDetailsFacadeTest extends UnitTest {

    @Mock
    ChooseStudentsMVP.Model chooseStudentModel;

    @Mock
    FileListMVP.Model fileListModel;

    private final static String courseId = "cid";
    private final static String termId = "tid";

    private FileDetailsMVP.FileDetailsFacade model;

    @Override
    protected void onSetup() {
        model = new FileDetailsFacade(chooseStudentModel, fileListModel, courseId, termId);
    }

    @Test
    public void loadStudentShares() throws Exception {
        Mockito.when(chooseStudentModel.provideListOfStudents(courseId, termId, true))
                .thenReturn(Observable.just(provideStudentShareDtos()));
        Mockito.when(fileListModel.getFilesDto(true, FilesOwnerType.ALL))
                .thenReturn(Observable.just(provideFileDTOs()));
        model.loadStudentShares(FILE_ID, true)
                .subscribe(studentShareDTOs -> {
                    assertTrue(studentShareDTOs.size() == 2);
                    assertTrue(studentShareDTOs.get(1).isChosen());
                    assertTrue(studentShareDTOs.get(1).isEnabled());
                });
    }

    @Test
    public void loadFileProperties() throws Exception {
        Mockito.when(fileListModel.getFilesDto(true, FilesOwnerType.ALL))
                .thenReturn(Observable.just(provideFileDTOs()));
        model.loadFileDetails(FILE_ID, true)
                .subscribe(fileDTO -> {
                    assertEquals(fileDTO.getFileId(), FILE_ID);
                    assertEquals(fileDTO.getNumberOfShares(), 1);
                });
    }

    private static final String STUDENT_ID = "1";
    private static final String FILE_ID = "id1";

    private List<StudentShareDto> provideStudentShareDtos() {
        List<StudentShareDto> studentShareDtos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            studentShareDtos.add(new StudentShareDto("Name" + i, Integer.toString(i), i % 2 == 0));
        }
        return studentShareDtos;

    }

    private List<FileDTO> provideFileDTOs() {
        List<FileDTO> files = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            KujonFile kujonFile = new KujonFile();
            kujonFile.contentType = "content" + i;
            kujonFile.shareType = ShareFileTargetType.LIST;
            kujonFile.fileName = "name" + i;
            kujonFile.fileId = "id" + i;
            kujonFile.firstName = "firstName" + i;
            kujonFile.createdTime = new Date();
            if (i == 1) {
                kujonFile.fileSharedWith = new String[]{STUDENT_ID};
            }
            files.add(new NormalFileType(kujonFile));
        }
        return files;
    }
}