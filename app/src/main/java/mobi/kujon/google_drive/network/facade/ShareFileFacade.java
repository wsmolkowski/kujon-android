package mobi.kujon.google_drive.network.facade;


import mobi.kujon.google_drive.model.json.ShareFileTarget;
import mobi.kujon.google_drive.model.json.SharedFile;
import mobi.kujon.google_drive.network.BackendWrapper;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFile;
import mobi.kujon.google_drive.network.api.ShareFileKujon;
import rx.Observable;

public class ShareFileFacade implements ShareFile {

    private ShareFileKujon shareFileKujon;
    private BackendWrapper<SharedFile> backendWrapper;

    public ShareFileFacade(ShareFileKujon shareFileKujon) {
        this.shareFileKujon = shareFileKujon;
        backendWrapper = new BackendWrapper<>();
    }

    @Override
    public Observable<SharedFile> shareFile(ShareFileTarget shareFileTarget) {
        return backendWrapper.doSomething(shareFileKujon.shareFile(shareFileTarget));
    }
}
