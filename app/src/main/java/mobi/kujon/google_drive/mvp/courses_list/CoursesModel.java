package mobi.kujon.google_drive.mvp.courses_list;


import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.model.dto.CourseDTO;
import mobi.kujon.google_drive.network.unwrapped_api.CoursesApi;
import rx.Observable;

@ActivityScope
public class CoursesModel implements CoursesMVP.Model {

    private CoursesApi coursesApi;

    @Inject
    public CoursesModel(CoursesApi coursesApi) {
        this.coursesApi = coursesApi;
    }

    @Override
    public Observable<List<CourseDTO>> loadCourses(@NonNull String semesterId, boolean refresh) {
        return coursesApi.getCoursesBySemesters(refresh)
                .flatMap(Observable::from)
                .filter(it-> it.getTermId().equals(semesterId))
                .map(courseWithTerms -> CourseDTO.convertCourses2CourseDTOs(courseWithTerms.getCourses()));
    }
    
}
