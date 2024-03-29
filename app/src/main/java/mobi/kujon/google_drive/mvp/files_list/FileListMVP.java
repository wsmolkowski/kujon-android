package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.json.KujonFile;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import mobi.kujon.google_drive.mvp.HandleException;
import mobi.kujon.google_drive.ui.dialogs.sort_strategy.SortStrategy;
import rx.Observable;

/**
 *
 */

public interface FileListMVP {


    interface LoadPresenter extends ClearSubscriptions {
        void loadListOfFiles(boolean reload, @FilesOwnerType int fileType, SortStrategy sortStrategy);
        void sortList(SortStrategy sortStrategy);
    }


    interface Model {
        Observable<List<KujonFile>> subscribe();
        void load(boolean reload);
        void clear();
    }

    interface FilesView extends HandleException {
        void listOfFilesLoaded(List<FileDTO> fileDTOs);

        void noFilesAdded();
    }

    interface DeleteView extends HandleException {
        void fileDeleted(String name);
    }

    interface DeletePresenter extends ClearSubscriptions {
        void deleteFile(String fileId, String name);
    }

    interface DeleteModel {
        Observable<String> deleteFile(String fileId);
    }
}
