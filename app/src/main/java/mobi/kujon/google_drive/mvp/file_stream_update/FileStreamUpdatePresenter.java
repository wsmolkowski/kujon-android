package mobi.kujon.google_drive.mvp.file_stream_update;

import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class FileStreamUpdatePresenter extends AbstractClearSubsriptions implements FileStreamUpdateMVP.Presenter{
    private FileStreamUpdateMVP.Model model;
    private SchedulersHolder holder;

    public FileStreamUpdatePresenter(FileStreamUpdateMVP.Model model, SchedulersHolder holder) {
        this.model = model;
        this.holder = holder;
    }



    @Override
    public void subscribeToStream(FileStreamUpdateMVP.View view) {
        addToSubsriptionList(this.model.subscribeToStream()
                .subscribeOn(holder.subscribe())
                .observeOn(holder.observ())
                .subscribe(it->{
                    view.onUpdate(it);
                }, Throwable::printStackTrace));
    }

}