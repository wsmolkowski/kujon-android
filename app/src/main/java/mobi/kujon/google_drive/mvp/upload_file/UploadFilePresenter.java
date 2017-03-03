package mobi.kujon.google_drive.mvp.upload_file;

import java.io.File;

import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.network.KujonException;
import mobi.kujon.google_drive.network.unwrapped_api.FileUploadApi;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class UploadFilePresenter extends AbstractClearSubsriptions implements UploadFileMVP.Presenter {

    private UploadFileMVP.View view;
    private FileUploadApi fileUploadApi;
    private SchedulersHolder schedulersHolder;
    private FileStreamUpdateMVP.Model model;

    public UploadFilePresenter(UploadFileMVP.View view, FileUploadApi fileUploadApi, SchedulersHolder schedulersHolder, FileStreamUpdateMVP.Model model) {
        this.view = view;
        this.fileUploadApi = fileUploadApi;
        this.schedulersHolder = schedulersHolder;
        this.model = model;
    }

    @Override
    public void uploadFile(DataForFileUpload dataForFileUpload, FileUploadDto fileUploadDto) {
        addToSubsriptionList(fileUploadApi.uploadFile(fileUploadDto, dataForFileUpload)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> {
                    this.model.updateStream(new FileUpdateDto(dataForFileUpload.getTitle(),100,true));
                    view.onFileUploaded(dataForFileUpload.getTitle());
                }, error -> {
                    handleError(error,dataForFileUpload.getTitle());
                    view.handleException(error);
                }));
    }

    @Override
    public void uploadFile(File file, FileUploadDto fileUploadDto, boolean deleteFileAfterUpload) {
        addToSubsriptionList(fileUploadApi.uploadFile(fileUploadDto, file)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(it -> {
                    String name = file.getName();
                    this.model.updateStream(new FileUpdateDto(name,100,true));
                    if(deleteFileAfterUpload)file.delete();
                }, error -> {
                    String name = file.getName();
                    if(deleteFileAfterUpload)file.delete();
                    handleError(error, name);
                }));
    }


    private void handleError(Throwable error, String name) {
        if(error.getCause() instanceof KujonException){
            String cause = error.getCause().getMessage();
            this.model.updateStream(new FileUpdateDto(name,100,true,cause));
        }else {
            this.model.updateStream(new FileUpdateDto(name,100,true,null));
        }
    }


}
