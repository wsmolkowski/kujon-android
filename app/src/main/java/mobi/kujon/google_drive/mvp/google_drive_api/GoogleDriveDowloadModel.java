package mobi.kujon.google_drive.mvp.google_drive_api;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;

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
    public Observable<DataForFileUpload> dowloadFile(DriveId fileId) {
        return Observable.just(fileId.asDriveFile())
                .map(driveFile -> {
                    DriveResource.MetadataResult mdRslt = driveFile.getMetadata(googleApiClient).await();
                    String title = mdRslt.getMetadata().getTitle();
                    return new InsideHelper(driveFile, getDownloadProgressListener(title),mdRslt);
                })
                .map(insideHelper ->{
                    DriveApi.DriveContentsResult driveContentsResult = insideHelper.driveFile.open(googleApiClient, DriveFile.MODE_READ_ONLY, insideHelper.downloadProgressListener).await();
                    return new InsideHelper2(insideHelper.metadataResult,driveContentsResult);
                })
                .map(insideHelper2 -> {
                    try {
                        return new DataForFileUpload(readFully(insideHelper2.driveContentsResult.getDriveContents().getInputStream()),
                                insideHelper2.metadataResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    @NonNull
    private DriveFile.DownloadProgressListener getDownloadProgressListener(String title) {
        return (bytesDownloaded, bytesExpected) -> {
            int progress = (int) (bytesDownloaded * 100 / bytesExpected)/2;
            if(bytesExpected==0){
                progress = 50;
            }
            model.updateStream(new FileUpdateDto(title, progress));
        };
    }

    private class InsideHelper {
        DriveFile driveFile;
        DriveFile.DownloadProgressListener downloadProgressListener;
        DriveResource.MetadataResult metadataResult;

         InsideHelper(DriveFile driveFile, DriveFile.DownloadProgressListener downloadProgressListener,DriveResource.MetadataResult metadataResult) {
            this.driveFile = driveFile;
            this.downloadProgressListener = downloadProgressListener;
            this.metadataResult = metadataResult;
        }
    }
    private class InsideHelper2{
        DriveResource.MetadataResult metadataResult;
        DriveApi.DriveContentsResult driveContentsResult;

         InsideHelper2(DriveResource.MetadataResult metadataResult, DriveApi.DriveContentsResult driveContentsResult) {
            this.metadataResult = metadataResult;
            this.driveContentsResult = driveContentsResult;
        }
    }


    private byte[] readFully(InputStream responseStream) throws IOException {
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
