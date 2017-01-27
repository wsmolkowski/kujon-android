package mobi.kujon.google_drive.mvp.google_drive_api;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import rx.Observable;

/**
 *
 */

public interface GoogleDriveDowloadMVP {

    interface Model{
        void setGoogleClient(GoogleApiClient googleClient);
        Observable<byte[]> dowloadFile(DriveId fileId);
    }
    interface Presenter{
        void uploadFile(FileUpdateDto fileUpdateDto,DriveId driveId);
    }
}
