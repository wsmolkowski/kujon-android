package mobi.kujon.google_drive.model.dto.file;

import android.support.annotation.DrawableRes;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.model.json.KujonFile;
import mobi.kujon.google_drive.model.json.UploadedFile;

/**
 *
 */

public class PptFileType extends FileDTO {
    public PptFileType(KujonFile kujonFile) {
        super(kujonFile);
    }

    public PptFileType(UploadedFile uploadedFile, FileUploadDto fileUploadDto, DataForFileUpload dataForFileUpload) {
        super(uploadedFile, fileUploadDto, dataForFileUpload);
    }

    @Override
    public @DrawableRes  int getImageIcon() {
        return R.drawable.ppt_icon;
    }

    @Override
    public int getContentType() {
        return R.string.presentation_file;
    }
}
