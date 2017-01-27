package mobi.kujon.google_drive.mvp.google_drive_api;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import rx.Observable;

/**
 *
 */

public class GoogleDriveDowloadModel implements GoogleDriveDowloadMVP.Model {


    private static final String TAG = "GOOGLE_DOWLOAD";
    private GoogleApiClient googleApiClient;
    private FileStreamUpdateMVP.Model model;

    public GoogleDriveDowloadModel(GoogleApiClient googleApiClient, FileStreamUpdateMVP.Model model) {
        this.googleApiClient = googleApiClient;
        this.model = model;
    }


    @Override
    public void setGoogleClient(GoogleApiClient googleClient) {
        this.googleApiClient = googleClient;
    }


    @Override
    public Observable<byte[]> dowloadFile(DriveId fileId) {
        return Observable.just(fileId.asDriveFile())
                .map(driveFile -> {
                    DriveResource.MetadataResult mdRslt = driveFile.getMetadata(googleApiClient).await();
                    String title = mdRslt.getMetadata().getTitle();
                    return new InsideHelper(driveFile, getDownloadProgressListener(title));
                })
                .map(insideHelper -> insideHelper.driveFile.open(googleApiClient, DriveFile.MODE_READ_ONLY, insideHelper.downloadProgressListener).await())
                .map(driveContents -> {
                    try {
                        return readFully(driveContents.getDriveContents().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }

                });
    }

    @NonNull
    private DriveFile.DownloadProgressListener getDownloadProgressListener(String title) {
        return (bytesDownloaded, bytesExpected) -> {
            int progress = (int) (bytesDownloaded * 100 / bytesExpected);
            model.updateStream(new FileUpdateDto(title, progress));
        };
    }

    private class InsideHelper {
        DriveFile driveFile;
        DriveFile.DownloadProgressListener downloadProgressListener;

        public InsideHelper(DriveFile driveFile, DriveFile.DownloadProgressListener downloadProgressListener) {
            this.driveFile = driveFile;
            this.downloadProgressListener = downloadProgressListener;
        }
    }


    public byte[] readFully(InputStream responseStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int read = responseStream.read();
            while (read != -1) {
                byteArrayOutputStream.write(read);
                read = responseStream.read();
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
