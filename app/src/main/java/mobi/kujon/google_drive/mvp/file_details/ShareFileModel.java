package mobi.kujon.google_drive.mvp.file_details;


import javax.inject.Inject;

import mobi.kujon.google_drive.dagger.scopes.ActivityScope;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.model.json.ShareFileTarget;
import mobi.kujon.google_drive.model.json.SharedFile;
import mobi.kujon.google_drive.network.unwrapped_api.ShareFileApi;
import rx.Observable;

public class ShareFileModel implements FileDetailsMVP.ShareFileModel {

    private ShareFileApi shareFileApi;

    public ShareFileModel(ShareFileApi shareFileApi) {
        this.shareFileApi = shareFileApi;
    }

    @Override
    public Observable<SharedFile> shareFile(FileShareDto fileShareDto) {
        return shareFileApi.shareFile(new ShareFileTarget(fileShareDto));
    }
}
