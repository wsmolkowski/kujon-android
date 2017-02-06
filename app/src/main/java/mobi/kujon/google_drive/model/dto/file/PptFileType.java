package mobi.kujon.google_drive.model.dto.file;

import android.support.annotation.DrawableRes;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.json.KujonFile;

/**
 *
 */

public class PptFileType extends FileDTO {
    public PptFileType(KujonFile kujonFile) {
        super(kujonFile);
    }

    @Override
    public @DrawableRes  int getImageIcon() {
        return R.drawable.ppt_icon;
    }
}
