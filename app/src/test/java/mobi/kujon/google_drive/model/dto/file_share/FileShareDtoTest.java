package mobi.kujon.google_drive.model.dto.file_share;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import mobi.kujon.UnitTest;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.model.dto.StudentShareDto;

import static org.junit.Assert.*;

public class FileShareDtoTest extends UnitTest {

    @Override
    protected void onSetup() {

    }

    private static final String FILE_ID = "fid";
    private static final String STUDENT2_ID = "2";

    @Test
    public void testConstructFromStudentListNone() {
        FileShareDto fileShareDto = new FileShareDto(FILE_ID, provideStudents(false, false));
        assertEquals(fileShareDto.getShareType(), ShareFileTargetType.NONE);
    }

    @Test
    public void testConstructFromStudentListList() {
        FileShareDto fileShareDto = new FileShareDto(FILE_ID, provideStudents(false, true));
        assertEquals(fileShareDto.getShareType(), ShareFileTargetType.LIST);
        assertEquals(fileShareDto.getStudentsListToShare().get(0), STUDENT2_ID);
    }

    @Test
    public void testConstructFromStudentListAll() {
        FileShareDto fileShareDto = new FileShareDto(FILE_ID, provideStudents(true, true));
        assertEquals(fileShareDto.getShareType(), ShareFileTargetType.ALL);
    }

    private List<StudentShareDto> provideStudents(boolean isFirstChosen, boolean isSecondChosen) {
        return Arrays.asList(new StudentShareDto("Bill", "1", isFirstChosen),
                new StudentShareDto("Jack", STUDENT2_ID, isSecondChosen));
    }

}