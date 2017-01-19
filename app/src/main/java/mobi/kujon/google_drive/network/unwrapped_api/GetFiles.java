package mobi.kujon.google_drive.network.unwrapped_api;

import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;
import rx.Observable;

/**
 *
 */

public interface GetFiles {

    Observable<List<KujonFile>> getAllFiles();


    Observable<List<KujonFile>> getFiles(String courseId,String termId);
}
