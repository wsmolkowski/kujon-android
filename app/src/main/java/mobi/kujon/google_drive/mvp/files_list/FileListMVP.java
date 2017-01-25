package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;
import rx.Observable;

/**
 *
 */

public interface FileListMVP {


    interface Presenter extends ClearSubscriptions {
        void loadListOfFiles(String semesterId, String subjectId, boolean reload,@FilesOwnerType int fileType);
    }


    interface Model {
        Observable<FileDTO> getFilesDto(String semesterId, String subjectId, boolean reload,@FilesOwnerType int fileType);
    }

    interface FilesView extends HandleException {
        void listOfFilesLoaded(List<FileDTO> fileDTOs);
    }
}
