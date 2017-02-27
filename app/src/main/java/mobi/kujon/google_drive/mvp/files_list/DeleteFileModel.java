package mobi.kujon.google_drive.mvp.files_list;

import mobi.kujon.google_drive.network.unwrapped_api.DeleteFileApi;
import rx.Observable;

/**
 *
 */

public class DeleteFileModel implements FileListMVP.DeleteModel {
    private DeleteFileApi deleteFileApi;
    private String courseId,termId;

    public DeleteFileModel(DeleteFileApi deleteFileApi, String courseId, String termId) {
        this.deleteFileApi = deleteFileApi;
        this.courseId = courseId;
        this.termId = termId;
    }

    @Override
    public Observable<String> deleteFile(String fileId) {
        return deleteFileApi.deleteFile(fileId,courseId,termId);
    }
}
