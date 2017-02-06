package mobi.kujon.filesharing.model.courses_list;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.kujon.google_drive.model.json.CourseWithTerms;
import mobi.kujon.network.json.Course;

public class CoursesModelTestHelper {
    public static final String COURSE_ID = "cid";
    public static final String COURSE_NAME = "cname";
    public static final String SEMESTER_ID = "sid";
    public static final String SOME_OTHER_DIFFERENT_TERM = "Some";

    public static List<CourseWithTerms> mockCoursesBySemesters() {
        Course course = new Course();
        course.courseId = COURSE_ID;
        course.courseName = COURSE_NAME;
        CourseWithTerms coursesInSemester = new CourseWithTerms();
        coursesInSemester.put(SEMESTER_ID, Collections.singletonList(course));
        List<CourseWithTerms> list  = new ArrayList<>();
        list.add(coursesInSemester);
        CourseWithTerms someOtherCourseInSemester = new CourseWithTerms();
        someOtherCourseInSemester.put(SOME_OTHER_DIFFERENT_TERM + " Other Different Term", Collections.singletonList(course));
        list.add(someOtherCourseInSemester);
        return Collections.singletonList(coursesInSemester);
    }
}
