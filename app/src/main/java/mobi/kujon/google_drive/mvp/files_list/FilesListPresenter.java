package mobi.kujon.google_drive.mvp.files_list;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file.FileDtoFactory;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.ui.dialogs.sort_strategy.SortStrategy;
import mobi.kujon.google_drive.utils.FilesFilter;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class FilesListPresenter extends AbstractClearSubsriptions implements FileListMVP.LoadPresenter {

    private final FilesFilter myFilesFilter;
    private FileListMVP.Model model;
    private FileListMVP.FilesView filesView;
    private SchedulersHolder schedulersHolder;
    private List<FileDTO> lastList;
    private SortStrategy sortStrategy;
    private
    @FilesOwnerType
    int fileType;

    public FilesListPresenter(FileListMVP.Model model, FileListMVP.FilesView filesView, SchedulersHolder schedulersHolder, FilesFilter myFilesFilter) {
        this.model = model;
        this.filesView = filesView;
        this.schedulersHolder = schedulersHolder;
        this.myFilesFilter = myFilesFilter;
    }

    private void subscribeToStream() {
        addToSubsriptionList(this.model.subscribe()
                .subscribeOn(this.schedulersHolder.subscribe())
                .map(it -> FileDtoFactory.createListOfDTOFiles(this.myFilesFilter.filterFiles(it, fileType)))
                .map(it -> sortStrategy.sort(it))
                .doOnNext(it -> lastList = it)
                .observeOn(this.schedulersHolder.observ())
                .subscribe((fileDTOs) -> {
                            this.filesView.listOfFilesLoaded(fileDTOs);
                        },
                        error -> {
                            if (error instanceof NoFileException || error.getCause() instanceof NoFileException) {
                                this.filesView.noFilesAdded();
                            } else {
                                this.filesView.handleException(error);
                            }
                            this.subscribeToStream();
                        }));
    }

    @Override
    public void loadListOfFiles(boolean reload, @FilesOwnerType int fileType, SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
        this.fileType = fileType;
        subscribeToStream();
        if(reload){
            model.load(true);
        }
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
    }
}
