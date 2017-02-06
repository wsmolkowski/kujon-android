package mobi.kujon.google_drive.mvp.file_details;


import java.util.List;

import mobi.kujon.google_drive.model.json.SharedFile;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import rx.Observable;

public interface FileDetailsFacade {

    Observable<List<DisableableStudentShareDTO>> loadStudentShares(String fileId, boolean refresh);

    Observable<FileDTO> loadFileProperties(String fileId, boolean refresh);

    Observable<SharedFile> shareFile(FileShareDto fileShareDto);
}
