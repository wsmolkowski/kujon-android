package mobi.kujon.google_drive.network.unwrapped_api;


import java.util.List;

import mobi.kujon.network.json.Participant;
import rx.Observable;

public interface StudentsInCourseApi {
    Observable<List<Participant>> getCourseStudents(boolean refresh, String courseId, String termId);
}
