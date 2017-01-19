package mobi.kujon.filesharing.presenters.semester_list;


import java.util.Arrays;
import java.util.List;

import mobi.kujon.google_drive.model.dto.SemesterDTO;

public class SemestersPresenterTestHelper {
    public static final String SEMESTER1_ID = "1";
    public static final String SEMESTER1_CODE = "a";
    public static final String SEMESTER2_ID = "2";
    public static final String SEMESTER2_CODE = "b";


    public static List<SemesterDTO> getSemestersResponse(boolean refresh) {
        List<SemesterDTO> semesters = Arrays.asList(new SemesterDTO(SEMESTER1_ID, SEMESTER1_CODE));
        if(refresh) {
            semesters.add(new SemesterDTO(SEMESTER2_ID, SEMESTER2_CODE));
        }
        return semesters;
    }
}
