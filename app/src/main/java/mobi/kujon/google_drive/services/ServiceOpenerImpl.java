package mobi.kujon.google_drive.services;

import android.app.Application;

import com.google.android.gms.drive.DriveId;
import com.google.gson.Gson;

import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
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
        DowloadUploadFileServices.startService(application,gson.toJson(fileUploadDto),driveId);
    }
}
