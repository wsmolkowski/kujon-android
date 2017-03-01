package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.ui.dialogs.sort_strategy.SortStrategy;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class FilesListPresenter extends AbstractClearSubsriptions implements FileListMVP.LoadPresenter {

    private FileListMVP.Model model;
    private FileListMVP.FilesView filesView;
    private SchedulersHolder schedulersHolder;
    private List<FileDTO> lastList;

    public FilesListPresenter(FileListMVP.Model model, FileListMVP.FilesView filesView, SchedulersHolder schedulersHolder) {
        this.model = model;
        this.filesView = filesView;
        this.schedulersHolder = schedulersHolder;
    }

    @Override
    public void loadListOfFiles(boolean reload, @FilesOwnerType int fileType, SortStrategy sortStrategy) {
        super.clearSubscriptions();
        addToSubsriptionList(model.getFilesDto(reload, fileType)
                .subscribeOn(schedulersHolder.subscribe())
                .map(sortStrategy::sort)
                .observeOn(schedulersHolder.observ())
                .doOnNext(it -> lastList = it)
                .subscribe(it -> filesView.listOfFilesLoaded(it), error -> {
                    if (error instanceof NoFileException || error.getCause() instanceof NoFileException) {
                        filesView.noFilesAdded();
                    } else {
                        filesView.handleException(error);
                    }
                }));
    }

    @Override
    public void sortList(SortStrategy sortStrategy) {
        if (lastList != null && lastList.size() > 1) {
            filesView.listOfFilesLoaded(sortStrategy.sort(lastList));
        }
    }

    @Override
    public void clearSubscriptions() {
        super.clearSubscriptions();
        model.clear();
    }
}
