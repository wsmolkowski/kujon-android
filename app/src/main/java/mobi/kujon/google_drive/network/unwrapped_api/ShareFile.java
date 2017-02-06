package mobi.kujon.google_drive.network.unwrapped_api;


import mobi.kujon.google_drive.model.json.ShareFileTarget;
import mobi.kujon.google_drive.model.json.SharedFile;
import rx.Observable;

public interface ShareFile {

    Observable<SharedFile> shareFile(ShareFileTarget shareFileTarget);
}
