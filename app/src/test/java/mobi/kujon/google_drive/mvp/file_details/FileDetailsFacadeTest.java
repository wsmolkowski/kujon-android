package mobi.kujon.google_drive.mvp.file_details;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.google_drive.model.ShareFileTargetType;
import mobi.kujon.google_drive.model.SharedFile;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.NormalFileType;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.files_list.FilesOwnerType;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFile;
import rx.Observable;

public class FileDetailsFacadeTest extends UnitTest {

    @Mock
    ChooseStudentsMVP.Model studentChoiceModel;

    @Mock
    FileListMVP.Model fileListModel;

    @Mock
    ShareFile shareFile;

    private final static String courseId = "cid";
    private final static String termId = "tid";
    private final static String fileId = "id" + 3;

    private FileDetailsFacade fileDetailsFacade;

    @Override
    protected void onSetup() {
        fileDetailsFacade = new FileDetailsFacadeImpl(studentChoiceModel, fileListModel, shareFile, courseId, termId);
    }

    @Test
    public void loadStudentShares() throws Exception {
        Mockito.when(studentChoiceModel.provideListOfStudents(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(Observable.just(provideStudentShares()));
        Mockito.when(fileListModel.getFilesDto(true, FilesOwnerType.ALL)).thenReturn(Observable.just(provideFileDTOs()));

        fileDetailsFacade.loadStudentShares(fileId, true)
                .subscribe(this::assertStudents);
    }

    private void assertStudents(List<DisableableStudentShareDTO> studentShareDTOs) {
        Assert.assertEquals(studentShareDTOs.size(), 5);
        for(int i = 0; i < 5; i++) {
            DisableableStudentShareDTO dto = studentShareDTOs.get(i);
            Assert.assertEquals(dto.isEnabled(), true);
            Assert.assertEquals(dto.getStudentShareDto().getStudentId(), "id"+i);
            Assert.assertEquals(dto.getStudentShareDto().getStudentName(), "Name"+i);
            Assert.assertEquals(dto.getStudentShareDto().isChosen(), i%2==0);
        }
    }

    private static List<StudentShareDto> provideStudentShares() {
        List<StudentShareDto> studentShareDtos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            studentShareDtos.add(new StudentShareDto("Name" + i, "id" + i, i % 2 == 0));
        }
        return studentShareDtos;
    }

    private static List<String> provideStudentSharesIds() {
        List<StudentShareDto> dtos = provideStudentShares();
        List<String> result = new ArrayList<>();
        for(StudentShareDto dto : dtos) {
            if(dto.isChosen()) {
                result.add(dto.getStudentId());
            }
        }
        return result;
    }

    private static List<FileDTO> provideFileDTOs() {
        List<FileDTO> files = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            KujonFile kujonFile = new KujonFile();
            kujonFile.contentType = "content" + i;
            kujonFile.fileName = "name" + i;
            kujonFile.fileId = "id" + i;
            kujonFile.firstName = "firstName" + i;
            kujonFile.createdTime = new Date();
            if(i == 3) {
                List<String> ids = provideStudentSharesIds();
                String[] shares = new String[ids.size()];
                kujonFile.fileSharedWith = ids.toArray(shares);
            }
            files.add(new NormalFileType(kujonFile));
        }
        return files;
    }

    @Test
    public void loadFileProperties() throws Exception {
        Mockito.when(fileListModel.getFilesDto(true, FilesOwnerType.ALL)).thenReturn(Observable.just(provideFileDTOs()));
        fileDetailsFacade.loadFileProperties(fileId, true)
                .subscribe(fileDTO -> {
                    Assert.assertEquals(fileDTO.getFileId(), fileId);
                });
    }

    private static final String fileSharedId = "1";
    private static final String StudentId = "13";

    @Test
    public void testShareFile() throws Exception {
        FileShareDto fileShareDto = new FileShareDto(fileSharedId, ShareFileTargetType.ALL, Collections.singletonList(StudentId));
        Mockito.when(shareFile.shareFile(Mockito.any())).thenReturn(Observable.just(provideSharedFile()));
        fileDetailsFacade.shareFile(fileShareDto).subscribe(sharedFile -> {
            Assert.assertEquals(sharedFile.fileId, fileSharedId);
            Assert.assertEquals(sharedFile.fileSharedWith.size(), 1);
            Assert.assertEquals(sharedFile.fileSharedWith.get(0), StudentId);
        });
    }

    private SharedFile provideSharedFile() {
        SharedFile file = new SharedFile();
        file.fileId = fileSharedId;
        file.fileSharedWith = Collections.singletonList(StudentId);
        return file;
    }

}