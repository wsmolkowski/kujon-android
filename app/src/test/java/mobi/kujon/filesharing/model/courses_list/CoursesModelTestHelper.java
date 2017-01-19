package mobi.kujon.filesharing.model.courses_list;


import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

import mobi.kujon.network.json.Course;

public class CoursesModelTestHelper {
    public static final String COURSE_ID = "cid";
    public static final String COURSE_NAME = "cname";
    public static final String SEMESTER_ID = "sid";

    public static List<SortedMap<String, List<Course>>> mockCoursesBySemesters() {
        Course course = new Course();
        course.courseId = COURSE_ID;
        course.courseName = COURSE_NAME;
        SortedMap<String, List<Course>> coursesInSemester = new ConcurrentSkipListMap<>();
        coursesInSemester.put(SEMESTER_ID, Collections.singletonList(course));
        return Collections.singletonList(coursesInSemester);
    }
}
