package mobi.kujon.filesharing.model.courses_list;


import android.support.v4.util.Pair;

import java.util.Collections;
import java.util.List;

import mobi.kujon.network.json.Course;

public class CoursesModelTestHelper {
    public static final String COURSE_ID = "cid";
    public static final String COURSE_NAME = "cname";
    public static final String SEMESTER_ID = "sid";

    public static List<Pair<String, List<Course>>> mockCoursesBySemesters() {
        Course course = new Course();
        course.courseId = COURSE_ID;
        course.courseName = COURSE_NAME;
        return Collections.singletonList(Pair.create(SEMESTER_ID, Collections.singletonList(course)));
    }
}
