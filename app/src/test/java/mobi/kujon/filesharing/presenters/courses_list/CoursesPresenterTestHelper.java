package mobi.kujon.filesharing.presenters.courses_list;


import java.util.Collections;
import java.util.List;

import mobi.kujon.google_drive.model.dto.CourseDTO;

public class CoursesPresenterTestHelper {

    public static final String COURSE_NAME = "nazwa";
    public static final String COURSE_ID = "abc";

    public static List<CourseDTO> mockCourses() {
        return Collections.singletonList(new CourseDTO(COURSE_NAME, COURSE_ID));
    }
}
