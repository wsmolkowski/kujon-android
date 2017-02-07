package mobi.kujon.google_drive.mvp.file_details;


import java.util.List;

import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

public class ShareFilePresenter extends AbstractClearSubsriptions implements FileDetailsMVP.ShareFilePresenter {

    private FileDetailsMVP.ShareFileModel model;
    private FileDetailsMVP.ShareView view;
    private SchedulersHolder schedulersHolder;

    public ShareFilePresenter(FileDetailsMVP.ShareFileModel model, FileDetailsMVP.ShareView view, SchedulersHolder schedulersHolder) {
        this.model = model;
        this.view = view;
        this.schedulersHolder = schedulersHolder;
    }

    @Override
    public void shareFileWith(String fileId, @ShareFileTargetType String targetType, List<StudentShareDto> shares) {
        addToSubsriptionList(model.shareFile(new FileShareDto(fileId, shares, targetType))
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(sharedFile ->
                                view.fileShared(sharedFile.shareType, sharedFile.fileSharedWith),
                        throwable -> {
                            view.shareFailed();
                            view.handleException(throwable);
                        }
                ));
    }
}
