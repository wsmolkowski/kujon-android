package mobi.kujon.google_drive.mvp.google_drive_api;

import com.google.android.gms.drive.DriveId;

import rx.Observable;

/**
 *
 */

public interface GoogleDriveDowloadMVP {

    interface Model{
        Observable<byte[]> dowloadFile(DriveId fileId);
    }
}
