package mobi.kujon.google_drive.mvp.file_stream_update;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.mvp.ClearSubscriptions;
import rx.Observable;

/**
 *
 */

public interface FileStreamUpdateMVP {


    interface Model{
        Observable<FileUpdateDto> subscribeToStream();
        void updateStream(FileUpdateDto fileUpdateDto);
    }

    interface Presenter extends ClearSubscriptions{
        void subscribeToStream();
    }

    interface View{
        void onUpdate(FileUpdateDto fileUpdateDto);
    }
}
