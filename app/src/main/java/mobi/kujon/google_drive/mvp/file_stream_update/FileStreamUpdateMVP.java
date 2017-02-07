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

    interface CancelModel{
        Observable<String> subscribeToCancelStream();
        void updateStream(String fileName);
    }

    interface Presenter extends ClearSubscriptions{
        void subscribeToStream(View view);
    }

    interface CancelPresenter extends ClearSubscriptions{
        void subscribeToStream(CancelView view);
    }

    interface CancelView{
        void onCancel(String fileName);
    }
    interface View{
        void onUpdate(FileUpdateDto fileUpdateDto);
    }
}
