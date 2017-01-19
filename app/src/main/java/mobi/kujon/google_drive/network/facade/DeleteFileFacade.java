package mobi.kujon.google_drive.network.facade;


import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.api.DeleteFileKujon;
import mobi.kujon.google_drive.network.unwrapped_api.DeleteFile;
import rx.Observable;

public class DeleteFileFacade implements DeleteFile {

    private DeleteFileKujon deleteFileKujon;
    private BackendWrapper<String> backendWrapper;

    public DeleteFileFacade(DeleteFileKujon deleteFileKujon) {
        this.deleteFileKujon = deleteFileKujon;
        backendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<String> deleteFile(String fileId) {
        return backendWrapper.doSmething(deleteFileKujon.deleteFile(fileId));
    }
}
