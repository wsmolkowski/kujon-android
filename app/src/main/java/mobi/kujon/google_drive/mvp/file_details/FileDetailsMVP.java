package mobi.kujon.google_drive.mvp.file_details;


import java.util.List;

import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_details.FileDetailsDto;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.model.json.SharedFile;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;
import rx.Observable;

public interface FileDetailsMVP {

    interface ShareFileModel {
        Observable<SharedFile> shareFile(FileShareDto fileShareDto);
    }

    interface FileDetailsModel {
        Observable<FileDetailsDto> loadFileDetails(String fileId, boolean refresh);
    }

    interface FileDetailsPresenter extends ClearSubscriptions {
        void loadFileDetails(String fileId, boolean refresh);
    }


    interface ShareFilePresenter extends ClearSubscriptions {
        void shareFileWith(String fileId, @ShareFileTargetType String targetType, List<StudentShareDto> shares);
    }

    interface FileDetailsView extends HandleException {
        void displayFileDetails(FileDetailsDto fileDetailsDto);
    }



    interface ShareView extends HandleException {
        void fileShared(String shareType, List<String> fileSharedWith);
        void shareFailed();
    }
}
