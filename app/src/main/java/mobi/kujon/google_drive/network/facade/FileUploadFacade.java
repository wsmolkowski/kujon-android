package mobi.kujon.google_drive.network.facade;


import java.util.List;

import mobi.kujon.google_drive.model.ShareFileTargetType;
import mobi.kujon.google_drive.model.UploadedFile;
import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.FileUploadKujon;
import mobi.kujon.google_drive.network.unwrapped_api.FileUpload;
import mobi.kujon.google_drive.utils.MultipartUtils;
import rx.Observable;

public class FileUploadFacade implements FileUpload {
    public static final String FILES_PART_NAME = "files";

    private FileUploadKujon fileUploadKujon;
    private MultipartUtils multipartUtils;
    private BackendWrapper<List<UploadedFile>> backendWrapper;

    public FileUploadFacade(FileUploadKujon fileUploadKujon, MultipartUtils multipartUtils) {
        this.fileUploadKujon = fileUploadKujon;
        this.multipartUtils = multipartUtils;
        backendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<List<UploadedFile>> uploadFile(String courseId,
                                                     String termId,
                                                     @ShareFileTargetType String shareWith,
                                                     List<Integer> sharedWithList,
                                                     String filePath) {
        return backendWrapper.doSmething(fileUploadKujon.uploadFile(
                multipartUtils.createPartFromString(courseId),
                multipartUtils.createPartFromString(termId),
                multipartUtils.createPartFromString(shareWith),
                multipartUtils.createPartFromCollection(sharedWithList),
                multipartUtils.prepareFilePart(FILES_PART_NAME, filePath)));
    }
}
