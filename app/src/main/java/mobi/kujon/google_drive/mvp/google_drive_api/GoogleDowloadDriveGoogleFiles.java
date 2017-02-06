package mobi.kujon.google_drive.mvp.google_drive_api;

import com.google.android.gms.drive.DriveId;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.Drive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.google_drive_api.GoogleDriveDowloadMVP.ModelGoogleFiles;
import rx.Observable;
import rx.exceptions.Exceptions;

/**
 *
 */

public class GoogleDowloadDriveGoogleFiles implements ModelGoogleFiles {
    private FileStreamUpdateMVP.Model model;
    private Drive mService;

    public GoogleDowloadDriveGoogleFiles(FileStreamUpdateMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setGoogleClient(com.google.api.services.drive.Drive googleClient) {
        mService = googleClient;
    }

    @Override
    public Observable<DataForFileUpload> dowloadFile(DriveId fileId) {


        return Observable.just(fileId.getResourceId())
                .map(it -> {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try {
                        mService.files()
                                .export(it, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                .executeMediaAndDownloadTo(outputStream);
                        return outputStream;
                    } catch (UserRecoverableAuthIOException e) {
                        throw Exceptions.propagate(e);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }

                }).map(it -> new DataForFileUpload(null, null, null));

    }
}
