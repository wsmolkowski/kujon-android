package mobi.kujon.google_drive.mvp.file_details;


import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

public class FileDetailsPresenter extends AbstractClearSubsriptions implements FileDetailsMVP.FileDetailsPresenter {

    private FileDetailsMVP.FileDetailsFacade fileDetailsFacade;
    private FileDetailsMVP.FileDetailsView view;
    private SchedulersHolder schedulersHolder;

    public FileDetailsPresenter(FileDetailsMVP.FileDetailsFacade fileDetailsFacade, FileDetailsMVP.FileDetailsView view, SchedulersHolder holder) {
        this.fileDetailsFacade = fileDetailsFacade;
        this.view = view;
        this.schedulersHolder = holder;
    }

    @Override
    public void loadFileDetails(String fileId, boolean refresh) {
        addToSubsriptionList(fileDetailsFacade.loadFileDetails(fileId, refresh)
                .observeOn(schedulersHolder.observ())
                .subscribeOn(schedulersHolder.subscribe())
                .subscribe(fileDTO -> view.displayFileProperties(fileDTO),
                        throwable -> view.handleException(throwable)));
    }
}
