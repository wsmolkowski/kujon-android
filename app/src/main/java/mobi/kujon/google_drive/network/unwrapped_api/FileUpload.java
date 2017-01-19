package mobi.kujon.google_drive.network.unwrapped_api;


import java.util.List;

import mobi.kujon.google_drive.model.ShareFileTargetType;
import mobi.kujon.google_drive.model.UploadedFile;
import rx.Observable;

public interface FileUpload {

    Observable<List<UploadedFile>> uploadFile(
            String courseId,
            String termId,
            @ShareFileTargetType String shareWith,
            List<Integer> sharedWithList,
            String filePath);
}
