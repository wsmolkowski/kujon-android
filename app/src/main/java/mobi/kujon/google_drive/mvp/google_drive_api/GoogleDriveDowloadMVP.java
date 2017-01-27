package mobi.kujon.google_drive.mvp.google_drive_api;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;

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
        void uploadFile(String courseId,String termId,byte[] bytes);
    }
}
