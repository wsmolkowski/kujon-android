package mobi.kujon.google_drive.mvp.files_list;

import javax.inject.Inject;

import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.network.unwrapped_api.DeleteFileApi;
import rx.Observable;

/**
 *
 */
@ActivityScope
public class DeleteFileModel implements FileListMVP.DeleteModel {
    private DeleteFileApi deleteFileApi;

    @Inject
    public DeleteFileModel(DeleteFileApi deleteFileApi) {
        this.deleteFileApi = deleteFileApi;
    }

    @Override
    public Observable<String> deleteFile(String fileId) {
        return deleteFileApi.deleteFile(fileId);
    }
}
