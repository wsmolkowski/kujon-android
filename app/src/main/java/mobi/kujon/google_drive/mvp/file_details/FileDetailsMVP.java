package mobi.kujon.google_drive.mvp.file_details;


import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
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

    interface FileDetailsFacade {
        Observable<List<DisableableStudentShareDTO>> loadStudentShares(String fileId, boolean refresh);

        Observable<FileDTO> loadFileDetails(String fileId, boolean refresh);
    }

    interface FileDetailsPresenter extends ClearSubscriptions {
        void loadFileDetails(String fileId, boolean refresh);
    }

    interface StudentsPresenter extends ClearSubscriptions {
        void chooseEveryoneToShare(boolean everyoneChosen, List<DisableableStudentShareDTO> shares);

        void loadStudents(String fileId, boolean refresh);
    }

    interface ShareFilePresenter extends ClearSubscriptions {
        void shareFileWith(String fileId, @ShareFileTargetType String targetType, List<DisableableStudentShareDTO> shares);
    }

    interface FileDetailsView extends HandleException {
        void displayFileProperties(FileDTO fileDTO);
    }

    interface StudentsView extends HandleException {
        void displayFileShares(List<DisableableStudentShareDTO> shares);
    }

    interface ShareView extends HandleException {
        void fileShared();
    }
}
