package mobi.kujon.google_drive.network.unwrapped_api;

import java.util.List;

import mobi.kujon.network.json.Term2;
import rx.Observable;

/**
 *
 */

public interface SemesterApi {
    Observable<List<Term2>> getSemesters(boolean b);
}
