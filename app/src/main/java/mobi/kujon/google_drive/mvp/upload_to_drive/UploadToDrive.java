package mobi.kujon.google_drive.mvp.upload_to_drive;

import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;

/**
 *
 */

public interface UploadToDrive {


    interface Presenter extends ClearSubscriptions{
        void uploadToDrive(FileUploadInfoDto fileUploadInfoDto,String driveFolderId);
    }

    interface View extends HandleException{
        void fileUploaded();
    }

}
