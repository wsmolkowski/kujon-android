package mobi.kujon.google_drive.mvp.files_list;

import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class FilesListPresenter extends AbstractClearSubsriptions implements FileListMVP.LoadPresenter {

    private FileListMVP.Model model;
    private FileListMVP.FilesView filesView;
    private SchedulersHolder schedulersHolder;

    public FilesListPresenter(FileListMVP.Model model, FileListMVP.FilesView filesView, SchedulersHolder schedulersHolder) {
        this.model = model;
        this.filesView = filesView;
        this.schedulersHolder = schedulersHolder;
    }

    @Override
    public void loadListOfFiles(boolean reload, @FilesOwnerType int fileType) {
        addToSubsriptionList(model.getFilesDto(reload, fileType)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> filesView.listOfFilesLoaded(it), error -> filesView.handleException(error)));
    }
}