package mobi.kujon.google_drive.mvp.file_details;


import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;

public interface FileDetailsMVP {

    interface Presenter extends ClearSubscriptions {
        void shareFileWith(String fileId, List<DisableableStudentShareDTO> shares);
        void chooseEveryoneToShare(boolean everyoneChosen, List<DisableableStudentShareDTO> shares);
        void loadFileDetails(String fileId, boolean refresh);
    }

    interface View extends HandleException {
        void displayFileProperties(FileDTO fileDTO);
        void displayFileShares(List<DisableableStudentShareDTO> shares);
        void fileShared();
    }
}
