package mobi.kujon.google_drive.mvp.google_drive_api;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import rx.Observable;

/**
 *
 */

public class GoogleDriveDowloadModel implements GoogleDriveDowloadMVP.ModelOtherFiles {


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
    public void setGoogleClient(GoogleDriveDowloadMVP.GoogleClientProvider googleClient) {
        this.googleApiClient = googleClient.getGoogleClient();
    }

    @Override
    public Observable<DataForFileUpload> dowloadFile(DriveId fileId, String mimeType, String title) {
        return Observable.just(fileId.asDriveFile())
                .doOnNext(driveFile -> {
                    maxBytes = driveFile.getMetadata(googleApiClient).await().getMetadata().getFileSize();
                })
                .map(driveFile -> driveFile.open(googleApiClient, DriveFile.MODE_READ_ONLY, getDownloadProgressListener(title)).await())
                .map(result -> {
                    try {
                        return new DataForFileUpload(readFully(result.getDriveContents().getInputStream(), title), mimeType, title);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    private int lastProgress = 0;
    private long maxBytes;

    @NonNull
    private DriveFile.DownloadProgressListener getDownloadProgressListener(String title) {
        return (bytesDownloaded, bytesExpected) -> {
            int progress = (int) (bytesDownloaded * 33 / bytesExpected);
            if (bytesExpected == 0) {
                progress = 33;
            }
            updateProgress(title, progress);

        };
    }

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    private byte[] readFully(InputStream responseStream, String title) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        long uploaded = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        try {
            int read;
            while ((read = responseStream.read(buffer)) != -1) {

                int progress = 34 + (int) (33 * uploaded / maxBytes);
                updateProgress(title, progress);
                uploaded += read;
                byteArrayOutputStream.write(buffer, 0, read);

            }
            model.updateStream(new FileUpdateDto(title, 67));
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void updateProgress(String title, int progress) {
        if (progress > lastProgress) {
            model.updateStream(new FileUpdateDto(title, progress));
            lastProgress = progress;
        }
    }
}
