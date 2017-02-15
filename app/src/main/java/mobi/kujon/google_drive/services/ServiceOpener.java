package mobi.kujon.google_drive.services;

import com.google.android.gms.drive.DriveId;

import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;

/**
 *
 */

public interface ServiceOpener {
    void openUploadService(FileUploadDto fileUploadDto,DriveId driveId);
    void openAddToDriveService(FileUploadInfoDto file, DriveId driveId);

    void openDowloadService(FileUploadInfoDto file);
}
