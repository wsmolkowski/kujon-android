package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.json.KujonFile;
import mobi.kujon.google_drive.network.unwrapped_api.GetFilesApi;
import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 *
 */

public class FileListModel implements FileListMVP.Model {
    private String courseId, termId;
    private GetFilesApi getFilesApi;
    private SchedulersHolder schedulersHolder;
    private BehaviorSubject<List<KujonFile>> subject;

    public FileListModel(String courseId, String termId, GetFilesApi getFilesApi, SchedulersHolder schedulersHolder) {
        this.courseId = courseId;
        this.termId = termId;
        this.getFilesApi = getFilesApi;
        this.schedulersHolder = schedulersHolder;
        this.subject = BehaviorSubject.create();
    }




    @Override
    public Observable<List<KujonFile>> subscribe() {
        return subject;
    }

    @Override
    public void load(boolean reload) {
        getFilesApi.getFiles(reload, courseId, termId)
                .subscribeOn(schedulersHolder.subscribe())
                .subscribe(it -> {
                    if (it.size() != 0) {
                        subject.onNext(it);
                    } else {
                        subject.onError(new NoFileException());
                        this.subject = BehaviorSubject.create();
                    }
                }, error -> {
                    subject.onError(error);
                    this.subject = BehaviorSubject.create();
                });
    }

    @Override
    public void clear() {
        subject.onCompleted();
    }


}
