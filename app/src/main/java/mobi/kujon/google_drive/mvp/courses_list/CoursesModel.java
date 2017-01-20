package mobi.kujon.google_drive.mvp.courses_list;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.dto.CourseDTO;
import mobi.kujon.google_drive.network.facade.CoursesApiFacade;
import mobi.kujon.network.json.Course;
import rx.Observable;

public class CoursesModel implements CoursesMVP.Model {

    private CoursesApiFacade coursesApiFacade;

    public CoursesModel(CoursesApiFacade coursesApiFacade) {
        this.coursesApiFacade = coursesApiFacade;
    }

    @Override
    public Observable<List<CourseDTO>> loadCourses(@NonNull String semesterId, boolean refresh) {
        return coursesApiFacade.getCoursesBySemesters(refresh)
                .flatMap(Observable::from)
                .filter(it-> it.getTermId().equals(semesterId))
                .map(courseWithTerms -> convertCourses2CourseDTOs(courseWithTerms.getCourses()));
    }

    private List<CourseDTO> convertCourses2CourseDTOs(List<Course> courses){
        List<CourseDTO> dtos = new ArrayList<>();
        for(Course course : courses) {
            dtos.add(new CourseDTO(course.courseName, course.courseId));
        }
        return dtos;
    }
}
