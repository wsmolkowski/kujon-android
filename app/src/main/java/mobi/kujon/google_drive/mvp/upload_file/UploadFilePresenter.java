package mobi.kujon.google_drive.mvp.upload_file;

import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.network.unwrapped_api.FileUpload;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class UploadFilePresenter extends AbstractClearSubsriptions implements UploadFileMVP.Presenter {

    private UploadFileMVP.View view;
    private FileUpload fileUpload;
    private SchedulersHolder schedulersHolder;

    public UploadFilePresenter(UploadFileMVP.View view, FileUpload fileUpload, SchedulersHolder schedulersHolder) {
        this.view = view;
        this.fileUpload = fileUpload;
        this.schedulersHolder = schedulersHolder;
    }

    @Override
    public void uploadFile(DataForFileUpload dataForFileUpload, FileUploadDto fileUploadDto) {
        addToSubsriptionList(fileUpload.uploadFile(fileUploadDto, dataForFileUpload)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> {
                    view.onFileUploaded();
                }, error -> {
                    view.handleException(error);
                }));
    }


}
