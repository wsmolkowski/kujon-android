package mobi.kujon.google_drive.network.facade;


import java.util.List;

import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.CourseDetailsApiKujon;
import mobi.kujon.google_drive.network.unwrapped_api.StudentsInCourseApi;
import mobi.kujon.network.json.CourseDetails;
import mobi.kujon.network.json.Participant;
import rx.Observable;

public class StudentsInCourseFacade implements StudentsInCourseApi {

    private CourseDetailsApiKujon courseDetailsApiKujon;
    private BackendWrapper<CourseDetails> backendWrapper;

    public StudentsInCourseFacade(CourseDetailsApiKujon courseDetailsApiKujon) {
        this.courseDetailsApiKujon = courseDetailsApiKujon;
        backendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<List<Participant>> getCourseStudents(boolean refresh, String courseId, String termId) {
        return backendWrapper.doSomething(courseDetailsApiKujon.getCourseDetails(refresh, courseId, termId))
                .map(courseDetails -> courseDetails.participants);
    }

}
