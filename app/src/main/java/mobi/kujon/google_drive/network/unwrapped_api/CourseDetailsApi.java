package mobi.kujon.google_drive.network.unwrapped_api;


import mobi.kujon.network.json.CourseDetails;
import rx.Observable;

public interface CourseDetailsApi {
    Observable<CourseDetails> getCourseDetails(boolean refresh, String courseId, String termId);
}
