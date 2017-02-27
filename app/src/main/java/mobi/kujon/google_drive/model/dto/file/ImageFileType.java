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
public class ImageFileType extends FileDTO {

    public ImageFileType(KujonFile kujonFile) {
        super(kujonFile);
    }

    public ImageFileType(UploadedFile uploadedFile, FileUploadDto fileUploadDto, DataForFileUpload dataForFileUpload) {
        super(uploadedFile, fileUploadDto, dataForFileUpload);
    }

    @Override
    public @DrawableRes int getImageIcon() {
        return R.drawable.img_icon;
    }

    @Override
    public int getContentType() {
        return R.string.image_file;
    }
}
