package mobi.kujon.google_drive.network.unwrapped_api;


import rx.Observable;

public interface DeleteFile {

    Observable<String> deleteFile(String fileId);
}
