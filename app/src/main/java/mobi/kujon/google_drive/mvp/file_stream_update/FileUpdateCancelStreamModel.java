package mobi.kujon.google_drive.mvp.file_stream_update;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 *
 */

public class FileUpdateCancelStreamModel implements FileStreamUpdateMVP.CancelModel {
    private PublishSubject<String> stream;

    public FileUpdateCancelStreamModel() {
        this.stream = PublishSubject.create();
    }


    @Override
    public Observable<String> subscribeToCancelStream() {
        return stream;
    }

    @Override
    public void updateStream(String fileName) {
        stream.onNext(fileName);
    }
}
