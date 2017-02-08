package mobi.kujon.google_drive.mvp.files_list;

import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class DeletePresenter extends AbstractClearSubsriptions implements FileListMVP.DeletePresenter {

    private FileListMVP.DeleteModel deleteModel;
    private FileListMVP.DeleteView deleteView;
    private SchedulersHolder schedulersHolder;

    public DeletePresenter(FileListMVP.DeleteModel deleteModel, FileListMVP.DeleteView deleteView, SchedulersHolder schedulersHolder) {
        this.deleteModel = deleteModel;
        this.deleteView = deleteView;
        this.schedulersHolder = schedulersHolder;
    }

    @Override
    public void deleteFile(String fileId) {
        addToSubsriptionList(deleteModel.deleteFile(fileId).subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(it->{
                    deleteView.fileDeleted();
                },error->{
                    deleteView.handleException(error);
                }));
    }
}
