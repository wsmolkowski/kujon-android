package mobi.kujon.google_drive.mvp.upload_file;

import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;

/**
 *
 */

public interface UploadFileMVP {

    interface Presenter extends ClearSubscriptions{
        void uploadFile(DataForFileUpload dataForFileUpload, FileUploadDto fileUploadDto);
    }
    interface View extends HandleException{
        void onFileUploaded();
    }
}
