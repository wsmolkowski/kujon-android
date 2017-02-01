package mobi.kujon.google_drive.services;

import com.google.android.gms.drive.DriveId;

import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;

/**
 *
 */

public interface ServiceOpener {
    void openUploadService(FileUploadDto fileUploadDto,DriveId driveId);
}
