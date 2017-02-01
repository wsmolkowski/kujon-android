package mobi.kujon.google_drive.network.facade;


import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.CourseDetailsApiKujon;
import mobi.kujon.google_drive.network.unwrapped_api.CourseDetailsApi;
import mobi.kujon.network.json.CourseDetails;
import rx.Observable;

public class CourseDetailsFacade implements CourseDetailsApi {

    private CourseDetailsApiKujon courseDetailsApiKujon;
    private BackendWrapper<CourseDetails> backendWrapper;

    public CourseDetailsFacade(CourseDetailsApiKujon courseDetailsApiKujon) {
        this.courseDetailsApiKujon = courseDetailsApiKujon;
        backendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<CourseDetails> getCourseDetails(boolean refresh, String courseId, String termId) {
        return backendWrapper.doSomething(courseDetailsApiKujon.getCourseDetails(refresh, courseId, termId));
    }

}
