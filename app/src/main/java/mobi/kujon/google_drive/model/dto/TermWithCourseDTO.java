package mobi.kujon.google_drive.model.dto;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.json.CourseWithTerms;
import mobi.kujon.network.json.Course;

/**
 *
 */

public class TermWithCourseDTO {

    String termName;
    List<CourseDTO> courseDTOs;

    public TermWithCourseDTO(String termName, List<Course> courses) {
        this.termName = termName;
        this.courseDTOs = new ArrayList<>();
        for(Course course:courses){
            this.courseDTOs.add(new CourseDTO(course));
        }
    }

    public TermWithCourseDTO(CourseWithTerms course) {
        this(course.getTermId(),course.getCourses());
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public List<CourseDTO> getCourseDTOs() {
        return courseDTOs;
    }

    public void setCourseDTOs(List<CourseDTO> courseDTOs) {
        this.courseDTOs = courseDTOs;
    }

    public static List<TermWithCourseDTO> convertCourses2CourseDTOs(List<CourseWithTerms> courseWithTermses){
        List<TermWithCourseDTO> dtos = new ArrayList<>();
        for(CourseWithTerms course : courseWithTermses) {
            dtos.add(new TermWithCourseDTO(course));
        }
        return dtos;
    }
}
