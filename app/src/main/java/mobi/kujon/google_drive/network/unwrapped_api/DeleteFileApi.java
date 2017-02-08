package mobi.kujon.google_drive.network.unwrapped_api;


import rx.Observable;

public interface DeleteFileApi {

    Observable<String> deleteFile(String fileId);
}
