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
public class AudioFileType extends FileDTO {
    public AudioFileType(KujonFile kujonFile) {
        super(kujonFile);
    }

    public AudioFileType(UploadedFile uploadedFile, FileUploadDto fileUploadDto, DataForFileUpload dataForFileUpload) {
        super(uploadedFile, fileUploadDto, dataForFileUpload);
    }

    @Override
    public @DrawableRes int getImageIcon() {
        return R.drawable.snd_icon;
    }

    @Override
    public int getContentType() {
        return R.string.audio_file;
    }
}
