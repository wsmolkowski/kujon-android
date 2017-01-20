package mobi.kujon.google_drive.mvp.courses_list;


import android.support.annotation.NonNull;

import java.util.List;

import mobi.kujon.google_drive.model.dto.CourseDTO;
import mobi.kujon.google_drive.network.facade.CoursesApiFacade;
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
                .map(courseWithTerms -> CourseDTO.convertCourses2CourseDTOs(courseWithTerms.getCourses()));
    }
    
}
