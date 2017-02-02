package mobi.kujon.google_drive.model.dto.file;

import android.support.annotation.DrawableRes;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.KujonFile;

/**
 *
 */
public class VideoFileType extends FileDTO {
    public VideoFileType(KujonFile kujonFile) {
        super(kujonFile);
    }

    @Override
    public @DrawableRes int getImageIcon() {
        return R.drawable.vid_icon;
    }
}
