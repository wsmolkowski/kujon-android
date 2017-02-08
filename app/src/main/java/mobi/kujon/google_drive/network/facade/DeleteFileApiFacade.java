package mobi.kujon.google_drive.network.facade;


import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.DeleteFileKujon;
import mobi.kujon.google_drive.network.unwrapped_api.DeleteFileApi;
import rx.Observable;

public class DeleteFileApiFacade implements DeleteFileApi {

    private DeleteFileKujon deleteFileKujon;
    private BackendWrapper<String> backendWrapper;

    public DeleteFileApiFacade(DeleteFileKujon deleteFileKujon) {
        this.deleteFileKujon = deleteFileKujon;
        backendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<String> deleteFile(String fileId) {
        return backendWrapper.doSomething(deleteFileKujon.deleteFile(fileId));
    }
}
