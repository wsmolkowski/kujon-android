package mobi.kujon.google_drive.model.dto.file;

import android.support.annotation.DrawableRes;

import mobi.kujon.R;
import mobi.kujon.google_drive.model.json.KujonFile;

/**
 *
 */
public class AudioFileType extends FileDTO {
    public AudioFileType(KujonFile kujonFile) {
        super(kujonFile);
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
