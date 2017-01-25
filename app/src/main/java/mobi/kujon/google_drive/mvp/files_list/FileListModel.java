package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.FileDtoFactory;
import mobi.kujon.google_drive.network.unwrapped_api.GetFiles;
import mobi.kujon.google_drive.utils.FilesFilter;
import rx.Observable;

/**
 *
 */

public class FileListModel implements FileListMVP.Model {
    private String courseId,termId;
    private GetFiles getFiles;
    private FilesFilter myFilesFilter;

    public FileListModel(String courseId, String termId, GetFiles getFiles, FilesFilter myFilesFilter) {
        this.courseId = courseId;
        this.termId = termId;
        this.getFiles = getFiles;
        this.myFilesFilter = myFilesFilter;
    }



    @Override
    public Observable<List<FileDTO>> getFilesDto(boolean reload, @FilesOwnerType int fileType) {
        return getFiles.getFiles(reload,courseId,termId)
                .map(it-> FileDtoFactory.createListOfDTOFiles(myFilesFilter.filterFiles(it,fileType))
        );
    }
}
