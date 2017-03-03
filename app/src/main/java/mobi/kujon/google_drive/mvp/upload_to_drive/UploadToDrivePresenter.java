package mobi.kujon.google_drive.mvp.upload_to_drive;

import android.support.annotation.NonNull;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Arrays;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.network.unwrapped_api.FileDownloadApi;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import mobi.kujon.google_drive.utils.TempFileCreator;

/**
 *
 */

public class UploadToDrivePresenter extends AbstractClearSubsriptions implements UploadToDrive.Presenter {


    private UploadToDrive.View view;
    private FileDownloadApi fileDownloadApi;
    private SchedulersHolder schedulersHolder;
    private TempFileCreator tempFileCreator;
    private FileStreamUpdateMVP.Model model;
    private Drive drive;

    public UploadToDrivePresenter(UploadToDrive.View view, FileDownloadApi fileDownloadApi, SchedulersHolder schedulersHolder, TempFileCreator tempFileCreator, FileStreamUpdateMVP.Model model) {
        this.view = view;
        this.fileDownloadApi = fileDownloadApi;
        this.schedulersHolder = schedulersHolder;
        this.tempFileCreator = tempFileCreator;
        this.model = model;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    @Override
    public void uploadToDrive(FileUploadInfoDto fileUploadInfoDto, String driveFolderId) {


        fileDownloadApi.downloadFile(fileUploadInfoDto.getId())
                .subscribeOn(schedulersHolder.subscribe())
                .map(responseBody -> tempFileCreator.writeToTempFile(responseBody, percent -> {
                    model.updateStream(new FileUpdateDto(fileUploadInfoDto.getName(), percent / 2));
                }))
                .map(file -> {
                    File fileMetadata = new File();
                    fileMetadata.setName(fileUploadInfoDto.getName());
                    fileMetadata.setMimeType(fileUploadInfoDto.getContentType());
                    fileMetadata.setParents(Arrays.asList(driveFolderId));
                    FileContent fileContent = new FileContent(fileUploadInfoDto.getContentType(), file);
                    try {
                        Drive.Files.Create fileCreation = drive.files().create(fileMetadata, fileContent);
                        fileCreation.getMediaHttpUploader().setProgressListener(getMediaHttpUploaderProgressListener(fileUploadInfoDto));
                        File execute = fileCreation.execute();
                        file.delete();
                        return execute;
                    } catch (UserRecoverableAuthIOException e) {
                        view.authenticate(e);
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }finally {
                        file.delete();
                    }
                })
                .subscribe(it -> {
                    view.fileUploaded();
                }, error -> {
                    model.updateStream(new FileUpdateDto(fileUploadInfoDto.getName(),100,true,"upload failed"));
                    view.handleException(error);
                });


    }

    @NonNull
    private MediaHttpUploaderProgressListener getMediaHttpUploaderProgressListener(FileUploadInfoDto fileUploadInfoDto) {
        return mediaHttpUploader -> {
            if (mediaHttpUploader == null) return;
            switch (mediaHttpUploader.getUploadState()) {
                case MEDIA_IN_PROGRESS:
                    double percent = mediaHttpUploader.getProgress() * 100;
                    model.updateStream(new FileUpdateDto(fileUploadInfoDto.getName(), (int) percent / 2));
                    break;
                case MEDIA_COMPLETE:
                    model.updateStream(new FileUpdateDto(fileUploadInfoDto.getName(), 100));
            }
        };
    }


}
