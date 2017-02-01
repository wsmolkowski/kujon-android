package mobi.kujon.google_drive.network.unwrapped_api;


import java.util.List;

import mobi.kujon.google_drive.model.ShareFileTargetType;
import mobi.kujon.google_drive.model.UploadedFile;
import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import rx.Observable;

public interface FileUpload {

    Observable<List<UploadedFile>> uploadFile(
            String courseId,
            String termId,
            @ShareFileTargetType String shareWith,
            List<String> sharedWithList,
            String filePath);

    Observable<List<UploadedFile>> uploadFile(
            String courseId,
            String termId,
            @ShareFileTargetType String shareWith,
            List<String> sharedWithList,
            DataForFileUpload dataForFileUpload);
    Observable<List<UploadedFile>> uploadFile(FileUploadDto fileUploadDto,
            DataForFileUpload dataForFileUpload);
}
