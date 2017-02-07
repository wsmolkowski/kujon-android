package mobi.kujon.google_drive.mvp.file_details;


import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

public class FileDetailsPresenter extends AbstractClearSubsriptions implements FileDetailsMVP.FileDetailsPresenter {

    private FileDetailsMVP.FileDetailsModel fileDetailsModel;
    private FileDetailsMVP.FileDetailsView view;
    private SchedulersHolder schedulersHolder;

    public FileDetailsPresenter(FileDetailsMVP.FileDetailsModel fileDetailsModel, FileDetailsMVP.FileDetailsView view, SchedulersHolder holder) {
        this.fileDetailsModel = fileDetailsModel;
        this.view = view;
        this.schedulersHolder = holder;
    }

    @Override
    public void loadFileDetails(String fileId, boolean refresh) {
        addToSubsriptionList(fileDetailsModel.loadFileDetails(fileId, refresh)
                .observeOn(schedulersHolder.observ())
                .subscribeOn(schedulersHolder.subscribe())
                .subscribe(fileDetailsDto -> view.displayFileDetails(fileDetailsDto),
                        throwable -> view.handleException(throwable)));
    }
}
