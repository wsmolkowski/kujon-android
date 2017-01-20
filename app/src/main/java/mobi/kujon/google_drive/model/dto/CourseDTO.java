package mobi.kujon.google_drive.model.dto;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.network.json.Course;

/**
 *
 */

public class CourseDTO {

    private String courseName, courseId;

    public CourseDTO(String courseName, String courseId) {
        this.courseName = courseName;
        this.courseId = courseId;
    }

    public CourseDTO(Course course) {
        this(course.courseName,course.courseId);
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseDTO)) return false;

        CourseDTO that = (CourseDTO) o;

        if (courseName != null ? !courseName.equals(that.courseName) : that.courseName != null)
            return false;
        return courseId != null ? courseId.equals(that.courseId) : that.courseId == null;

    }

    @Override
    public int hashCode() {
        int result = courseName != null ? courseName.hashCode() : 0;
        result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
        return result;
    }

    public static List<CourseDTO> convertCourses2CourseDTOs(List<Course> courses){
        List<CourseDTO> dtos = new ArrayList<>();
        for(Course course : courses) {
            dtos.add(new CourseDTO(course));
        }
        return dtos;
    }
}
