package mobi.kujon.google_drive.mvp.file_stream_update;

import java.util.concurrent.TimeUnit;

import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class FileCancelPresenter extends AbstractClearSubsriptions implements FileStreamUpdateMVP.CancelPresenter {
    private FileStreamUpdateMVP.CancelModel model;
    private SchedulersHolder holder;

    public FileCancelPresenter(FileStreamUpdateMVP.CancelModel model, SchedulersHolder holder) {
        this.model = model;
        this.holder = holder;
    }



    @Override
    public void subscribeToStream(FileStreamUpdateMVP.CancelView view) {
        addToSubsriptionList(this.model.subscribeToCancelStream()
                .subscribeOn(holder.subscribe())
                .debounce(50, TimeUnit.MILLISECONDS)
                .observeOn(holder.observ())
                .subscribe(view::onCancel, Throwable::printStackTrace));
    }
}
