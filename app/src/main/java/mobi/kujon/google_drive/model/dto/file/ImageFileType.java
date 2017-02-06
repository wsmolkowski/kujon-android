package mobi.kujon.google_drive.model.dto.file;

import android.support.annotation.DrawableRes;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.json.KujonFile;

/**
 *
 */
public class ImageFileType extends FileDTO {

    public ImageFileType(KujonFile kujonFile) {
        super(kujonFile);
    }

    @Override
    public @DrawableRes int getImageIcon() {
        return R.drawable.img_icon;
    }
}
