package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.FileDtoFactory;
import mobi.kujon.google_drive.network.unwrapped_api.GetFiles;
import mobi.kujon.google_drive.utils.FilesFilter;
import mobi.kujon.utils.user_data.UserDataFacade;
import rx.Observable;
import rx.exceptions.Exceptions;

/**
 *
 */

public class FileListModel implements FileListMVP.Model {
    private String courseId, termId;
    private GetFiles getFiles;
    private FilesFilter myFilesFilter;
    private UserDataFacade userDataFacade;

    public FileListModel(String courseId, String termId, GetFiles getFiles, FilesFilter myFilesFilter, UserDataFacade userDataFacade) {
        this.courseId = courseId;
        this.termId = termId;
        this.getFiles = getFiles;
        this.myFilesFilter = myFilesFilter;
        this.userDataFacade = userDataFacade;
    }


    @Override
    public Observable<List<FileDTO>> getFilesDto(boolean reload, @FilesOwnerType int fileType) {
        return getFiles.getFiles(reload, courseId, termId)
                .map(it -> {
                    if (it.size() != 0) {
                        return FileDtoFactory.createListOfDTOFiles(myFilesFilter.filterFiles(it, fileType));
                    } else {
                        throw Exceptions.propagate(new NoFileException());
                    }
                });
    }


}
