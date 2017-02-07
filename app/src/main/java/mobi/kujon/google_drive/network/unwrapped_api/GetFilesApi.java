package mobi.kujon.google_drive.network.unwrapped_api;

import java.util.List;

import mobi.kujon.google_drive.model.json.KujonFile;
import rx.Observable;

/**
 *
 */

public interface GetFilesApi {

    Observable<List<KujonFile>> getAllFiles(boolean refresh);


    Observable<List<KujonFile>> getFiles(boolean refresh,String courseId,String termId);
}
