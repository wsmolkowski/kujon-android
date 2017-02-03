package mobi.kujon.google_drive.mvp.file_details;


import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import rx.Observable;

public interface FileDetailsFacade {

    Observable<List<DisableableStudentShareDTO>> loadStudentShares(boolean enabled, List<String> sharedWith);

    Observable<FileDTO> loadFileProperties(String fileId);
}
