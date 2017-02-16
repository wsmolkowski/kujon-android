package mobi.kujon.google_drive.mvp.courses_list;


import java.util.List;

import javax.inject.Inject;

import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.model.dto.TermWithCourseDTO;
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
    public Observable<List<TermWithCourseDTO>> loadCourses(boolean refresh) {
        return coursesApi.getCoursesBySemesters(refresh)
                .map(courseWithTerms -> TermWithCourseDTO.convertCourses2CourseDTOs(courseWithTerms));
    }
}
