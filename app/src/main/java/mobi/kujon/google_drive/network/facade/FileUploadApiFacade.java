package mobi.kujon.google_drive.network.facade;


import java.io.File;
import java.util.List;

import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.model.json.UploadedFile;
import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.model.dto.file_upload.DataForFileUpload;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.model.request.ProgressRequestBody;
import mobi.kujon.google_drive.model.request.ProgressRequestBodyFile;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.FileUploadKujon;
import mobi.kujon.google_drive.network.unwrapped_api.FileUploadApi;
import mobi.kujon.google_drive.utils.MultipartUtils;
import okhttp3.RequestBody;
import rx.Observable;

public class FileUploadApiFacade implements FileUploadApi {
    public static final String FILES_PART_NAME = "files";

    private FileUploadKujon fileUploadKujon;
    private MultipartUtils multipartUtils;
    private BackendWrapper<List<UploadedFile>> backendWrapper;
    private FileStreamUpdateMVP.Model updateModel;

    public FileUploadApiFacade(FileUploadKujon fileUploadKujon, MultipartUtils multipartUtils, FileStreamUpdateMVP.Model model) {
        this.fileUploadKujon = fileUploadKujon;
        this.multipartUtils = multipartUtils;
        backendWrapper = new BackendWrapper<>();
        this.updateModel = model;
    }

    @Override
    public Observable<List<UploadedFile>> uploadFile(String courseId,
                                                     String termId,
                                                     @ShareFileTargetType String shareWith,
                                                     List<String> sharedWithList,
                                                     String filePath) {
        return backendWrapper.doSomething(fileUploadKujon.uploadFile(
                multipartUtils.createPartFromString(courseId),
                multipartUtils.createPartFromString(termId),
                multipartUtils.createPartFromString(shareWith),
                multipartUtils.createPartFromCollection(sharedWithList),
                multipartUtils.prepareFilePart(FILES_PART_NAME, filePath)));
    }

    @Override
    public Observable<List<UploadedFile>> uploadFile(String courseId, String termId, @ShareFileTargetType String shareWith, List<String> sharedWithList, DataForFileUpload dataForFileUpload) {
        ProgressRequestBody.UploadCallbacks callbacks = percentage -> updateModel.updateStream(new FileUpdateDto(dataForFileUpload.getTitle(),percentage));
        return backendWrapper.doSomething(fileUploadKujon.uploadFile(
                multipartUtils.createPartFromString(courseId),
                multipartUtils.createPartFromString(termId),
                multipartUtils.createPartFromString(shareWith),
                multipartUtils.createPartFromCollection(sharedWithList),
                multipartUtils.prepareFilePart(FILES_PART_NAME, dataForFileUpload, callbacks)));
    }

    @Override
    public Observable<List<UploadedFile>> uploadFile(FileUploadDto fileUploadDto, DataForFileUpload dataForFileUpload) {
        return this.uploadFile(fileUploadDto.getCourseId(),fileUploadDto.getTermId(),fileUploadDto.getShareFileTargetType(),fileUploadDto.getListOfShares(),dataForFileUpload);
    }

    @Override
    public Observable<List<UploadedFile>> uploadFile(FileUploadDto fileUploadDto, File file) {
        ProgressRequestBody.UploadCallbacks callbacks = percentage -> updateModel.updateStream(new FileUpdateDto(file.getName(),percentage));
        ProgressRequestBodyFile progressRequestBodyFile = new ProgressRequestBodyFile(RequestBody.create(okhttp3.MultipartBody.FORM, file),callbacks);
        multipartUtils.prepareFilePart(FILES_PART_NAME, file.getName(), progressRequestBodyFile);
        return backendWrapper.doSomething(fileUploadKujon.uploadFile(
                multipartUtils.createPartFromString(fileUploadDto.getCourseId()),
                multipartUtils.createPartFromString(fileUploadDto.getTermId()),
                multipartUtils.createPartFromString(fileUploadDto.getShareFileTargetType()),
                multipartUtils.createPartFromCollection(fileUploadDto.getListOfShares()),
                multipartUtils.prepareFilePart(FILES_PART_NAME, file.getName(), progressRequestBodyFile)));
    }
}
