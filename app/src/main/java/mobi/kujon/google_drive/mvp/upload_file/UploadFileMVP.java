package mobi.kujon.google_drive.mvp.upload_file;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;

/**
 *
 */

public class UploadFileMVP {

    interface Presenter extends ClearSubscriptions{
        void uploadFile(byte[] bites, FileUpdateDto fileUpdateDto);
    }

}
