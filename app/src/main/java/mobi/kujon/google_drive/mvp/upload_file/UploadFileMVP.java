package mobi.kujon.google_drive.mvp.upload_file;

import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;

/**
 *
 */

public interface UploadFileMVP {

    interface Presenter extends ClearSubscriptions{
        void uploadFile(DataForFileUpload dataForFileUpload, FileUploadDto fileUploadDto);
    }

}
