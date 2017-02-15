package mobi.kujon.google_drive.services;

import android.app.Application;

import com.google.android.gms.drive.DriveId;
import com.google.gson.Gson;

import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;
import mobi.kujon.google_drive.services.add_to_google_drive.AddToGoogleDriveService;
import mobi.kujon.google_drive.services.dowload_file.DowloadFileService;
import mobi.kujon.google_drive.services.upload.DowloadUploadFileServices;

/**
 *
 */

public class ServiceOpenerImpl implements ServiceOpener {

    private Application application;
    private Gson gson;

    public ServiceOpenerImpl(Application application, Gson gson) {
        this.application = application;
        this.gson = gson;
    }

    @Override
    public void openUploadService(FileUploadDto fileUploadDto, DriveId driveId) {
        DowloadUploadFileServices.startService(application, gson.toJson(fileUploadDto), driveId);
    }

    @Override
    public void openAddToDriveService(FileUploadInfoDto file, DriveId driveId) {
        AddToGoogleDriveService.startService(application, file, driveId);
    }

    @Override
    public void openDowloadService(FileUploadInfoDto file) {
        DowloadFileService.startService(application, file.getId(), file.getName());
    }
}
