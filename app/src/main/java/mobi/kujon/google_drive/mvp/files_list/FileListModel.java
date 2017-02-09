package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.FileDtoFactory;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import mobi.kujon.google_drive.utils.FilesFilter;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.subjects.PublishSubject;

/**
 *
 */

public class FileListModel implements FileListMVP.Model {
    private String courseId, termId;
    private GetFilesApi getFilesApi;
    private FilesFilter myFilesFilter;
    private SchedulersHolder schedulersHolder;
    private PublishSubject<List<FileDTO>> subject;

    public FileListModel(String courseId, String termId, GetFilesApi getFilesApi, FilesFilter myFilesFilter, SchedulersHolder schedulersHolder) {
        this.courseId = courseId;
        this.termId = termId;
        this.getFilesApi = getFilesApi;
        this.myFilesFilter = myFilesFilter;
        this.schedulersHolder = schedulersHolder;
    }




    @Override
    public synchronized Observable<List<FileDTO>> getFilesDto(boolean reload, @FilesOwnerType int fileType) {
        if (subject == null || reload) {
            getFilesApi.getFiles(reload, courseId, termId)
                    .subscribeOn(schedulersHolder.subscribe())
                    .observeOn(schedulersHolder.subscribe())
                    .subscribe(it -> {
                        if (it.size() != 0) {
                            subject.onNext(FileDtoFactory.createListOfDTOFiles(myFilesFilter.filterFiles(it, fileType)));
                            subject.onCompleted();
                        } else {
                            subject.onError(Exceptions.propagate(new NoFileException()));
                        }
                    }, error -> {
                        subject.onError(Exceptions.propagate(new NoFileException()));
                    });
            subject = PublishSubject.create();
        }
        return subject;
    }


}
