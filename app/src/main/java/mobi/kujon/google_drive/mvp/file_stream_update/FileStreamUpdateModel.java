package mobi.kujon.google_drive.mvp.file_stream_update;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 *
 */

public class FileStreamUpdateModel implements FileStreamUpdateMVP.Model {

    private PublishSubject<FileUpdateDto> stream;

    public FileStreamUpdateModel() {
        this.stream = PublishSubject.create();
    }

    @Override
    public Observable<FileUpdateDto> subscribeToStream() {
        return stream;
    }

    @Override
    public void updateStream(FileUpdateDto fileUpdateDto) {
        stream.onNext(fileUpdateDto);
    }
}
