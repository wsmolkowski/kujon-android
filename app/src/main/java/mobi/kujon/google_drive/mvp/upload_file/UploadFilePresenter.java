package mobi.kujon.google_drive.mvp.upload_file;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.network.unwrapped_api.FileUpload;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class UploadFilePresenter extends AbstractClearSubsriptions implements UploadFileMVP.Presenter {

    private UploadFileMVP.View view;
    private FileUpload fileUpload;
    private SchedulersHolder schedulersHolder;
    private FileStreamUpdateMVP.Model model;

    public UploadFilePresenter(UploadFileMVP.View view, FileUpload fileUpload, SchedulersHolder schedulersHolder,FileStreamUpdateMVP.Model model) {
        this.view = view;
        this.fileUpload = fileUpload;
        this.schedulersHolder = schedulersHolder;
        this.model = model;
    }

    @Override
    public void uploadFile(DataForFileUpload dataForFileUpload, FileUploadDto fileUploadDto) {
        addToSubsriptionList(fileUpload.uploadFile(fileUploadDto, dataForFileUpload)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> {
                    this.model.updateStream(new FileUpdateDto(dataForFileUpload.getTitle(),100,true));
                    view.onFileUploaded();
                }, error -> {
                    this.model.updateStream(new FileUpdateDto(dataForFileUpload.getTitle(),100,true,true));
                    view.handleException(error);
                }));
    }


}
