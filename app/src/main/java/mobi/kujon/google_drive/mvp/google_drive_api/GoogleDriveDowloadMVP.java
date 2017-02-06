package mobi.kujon.google_drive.mvp.google_drive_api;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;

import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import rx.Observable;

/**
 *
 */

public interface GoogleDriveDowloadMVP {

    interface ModelOtherFiles extends Model{
        void setGoogleClient(GoogleApiClient googleClient);
    }

    interface ModelGoogleFiles extends Model{
        void setGoogleClient(com.google.api.services.drive.Drive googleClient);

    }

    interface Model{
        Observable<DataForFileUpload> dowloadFile(DriveId fileId);
    }

}
