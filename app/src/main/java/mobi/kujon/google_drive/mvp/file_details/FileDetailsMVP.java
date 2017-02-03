package mobi.kujon.google_drive.mvp.file_details;


import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;

public interface FileDetailsMVP {

    interface Presenter {
        void shareFileWith(FileShareDto fileShareDto);
        void chooseEveryoneToShare(boolean everyoneChosen);
        void loadFileDetails();
    }

    interface View {
        void displayFileProperties(FileDTO fileDTO);
        void displayFileShares(List<DisableableStudentShareDTO> shares);
    }
}
