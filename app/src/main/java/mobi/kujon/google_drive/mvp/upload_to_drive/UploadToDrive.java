package mobi.kujon.google_drive.mvp.upload_to_drive;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.Drive;

import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;

/**
 *
 */

public interface UploadToDrive {


    interface Presenter extends ClearSubscriptions{
        void uploadToDrive(FileUploadInfoDto fileUploadInfoDto,String driveFolderId);
        void setDrive(Drive drive);
    }

    interface View extends HandleException{
        void fileUploaded();

        void authenticate(UserRecoverableAuthIOException e);
    }

}
