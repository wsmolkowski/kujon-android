package mobi.kujon.google_drive.mvp.file_details;


import java.util.ArrayList;
import java.util.List;

import mobi.kujon.google_drive.model.ShareFileTargetType;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.dto.file_share.FileShareDto;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

public class FileDetailsPresenter extends AbstractClearSubsriptions implements FileDetailsMVP.Presenter {

    private FileDetailsFacade fileDetailsFacade;
    private FileDetailsMVP.View view;
    private SchedulersHolder schedulersHolder;

    public FileDetailsPresenter(FileDetailsFacade fileDetailsFacade, FileDetailsMVP.View view, SchedulersHolder holder) {
        this.fileDetailsFacade = fileDetailsFacade;
        this.view = view;
        this.schedulersHolder = holder;
    }

    @Override
    public void shareFileWith(String fileId, List<DisableableStudentShareDTO> shares) {
        FileShareDto fileShareDto = createFileShareDTO(fileId, shares);
        addToSubsriptionList(fileDetailsFacade.shareFile(fileShareDto)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(sharedFile ->
                                view.fileShared(),
                        throwable ->
                                view.handleException(throwable)));
    }

    private FileShareDto createFileShareDTO(String fileId, List<DisableableStudentShareDTO> shares) {
        String target = ShareFileTargetType.NONE;
        List<String> ids = new ArrayList<>();
        for (DisableableStudentShareDTO dto : shares) {
            if (dto.getStudentShareDto().isChosen()) {
                ids.add(dto.getStudentShareDto().getStudentId());
            }
        }
        if (ids.size() > 0) {
            target = ShareFileTargetType.LIST;
        }
        if (ids.size() == shares.size()) {
            target = ShareFileTargetType.ALL;
        }
        return new FileShareDto(fileId, target, ids);
    }

    @Override
    public void chooseEveryoneToShare(boolean everyoneChosen, List<DisableableStudentShareDTO> shares) {
        for(DisableableStudentShareDTO dto : shares){
            dto.setEnabled(!everyoneChosen);
        }
        view.displayFileShares(shares);
    }

    @Override
    public void loadFileDetails(String fileId, boolean refresh) {
        addToSubsriptionList(fileDetailsFacade.loadFileProperties(fileId, refresh)
                .observeOn(schedulersHolder.observ())
                .subscribeOn(schedulersHolder.subscribe())
                .subscribe(fileDTO -> view.displayFileProperties(fileDTO),
                        throwable -> view.handleException(throwable)));
        addToSubsriptionList(fileDetailsFacade.loadStudentShares(fileId, refresh)
                .observeOn(schedulersHolder.observ())
                .subscribeOn(schedulersHolder.subscribe())
                .subscribe(studentShareDTOs -> view.displayFileShares(studentShareDTOs),
                        throwable -> view.handleException(throwable)));
    }
}
