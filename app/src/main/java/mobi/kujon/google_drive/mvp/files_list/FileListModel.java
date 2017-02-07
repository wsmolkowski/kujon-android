package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.FileDtoFactory;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import mobi.kujon.google_drive.utils.FilesFilter;
import rx.Observable;
import rx.exceptions.Exceptions;

/**
 *
 */

public class FileListModel implements FileListMVP.Model {
    private String courseId, termId;
    private GetFilesApi getFilesApi;
    private FilesFilter myFilesFilter;

    public FileListModel(String courseId, String termId, GetFilesApi getFilesApi, FilesFilter myFilesFilter) {
        this.courseId = courseId;
        this.termId = termId;
        this.getFilesApi = getFilesApi;
        this.myFilesFilter = myFilesFilter;
    }


    @Override
    public Observable<List<FileDTO>> getFilesDto(boolean reload, @FilesOwnerType int fileType) {
        return getFilesApi.getFiles(reload, courseId, termId)
                .map(it -> {
                    if (it.size() != 0) {
                        return FileDtoFactory.createListOfDTOFiles(myFilesFilter.filterFiles(it, fileType));
                    } else {
                        throw Exceptions.propagate(new NoFileException());
                    }
                });
    }


}
