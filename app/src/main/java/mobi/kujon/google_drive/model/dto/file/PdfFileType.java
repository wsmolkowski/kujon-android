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

public class PdfFileType extends FileDTO {
    public PdfFileType(KujonFile kujonFile) {
        super(kujonFile);
    }

    public PdfFileType(UploadedFile uploadedFile, FileUploadDto fileUploadDto, DataForFileUpload dataForFileUpload) {
        super(uploadedFile, fileUploadDto, dataForFileUpload);
    }

    @Override
    public @DrawableRes int getImageIcon() {
        return R.drawable.pdf_icon;
    }

    @Override
    public int getContentType() {
        return R.string.pdf_file;
    }
}
