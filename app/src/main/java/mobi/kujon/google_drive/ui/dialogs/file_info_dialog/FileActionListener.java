package mobi.kujon.google_drive.ui.dialogs.file_info_dialog;

import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;

/**
 *
 */

public interface FileActionListener {
    void onFileDelete(String fileId);
    void onFileAddToGoogleDrive(FileUploadInfoDto  fileUploadInfoDto);
    void onFileDetails(String fileId);
    void onDownload(FileUploadInfoDto file);
}
