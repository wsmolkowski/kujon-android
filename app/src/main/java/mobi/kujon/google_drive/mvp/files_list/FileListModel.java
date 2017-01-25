package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.FileDtoFabric;
import mobi.kujon.google_drive.network.unwrapped_api.GetFiles;
import mobi.kujon.google_drive.utils.MyFilesFilter;
import rx.Observable;

/**
 *
 */

public class FileListModel implements FileListMVP.Model {
    private String courseId,termId;
    private GetFiles getFiles;
    private MyFilesFilter myFilesFilter;

    public FileListModel(String courseId, String termId, GetFiles getFiles) {
        this.courseId = courseId;
        this.termId = termId;
        this.getFiles = getFiles;
        this.myFilesFilter = new MyFilesFilter();
    }

    @Override
    public Observable<List<FileDTO>> getFilesDto(boolean reload, @FilesOwnerType int fileType) {
        return getFiles.getFiles(reload,courseId,termId)
                .map(it-> FileDtoFabric.createListOfDTOFiles(myFilesFilter.filterFiles(it,fileType))
        );
    }
}
