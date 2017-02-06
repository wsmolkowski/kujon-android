package mobi.kujon.google_drive.mvp.file_details;


import java.util.List;

import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

public class StudentsPresenter extends AbstractClearSubsriptions implements FileDetailsMVP.StudentsPresenter {

    private FileDetailsMVP.FileDetailsFacade model;
    private FileDetailsMVP.StudentsView view;
    private SchedulersHolder schedulersHolder;

    public StudentsPresenter(FileDetailsMVP.FileDetailsFacade model, FileDetailsMVP.StudentsView view, SchedulersHolder schedulersHolder) {
        this.model = model;
        this.view = view;
        this.schedulersHolder = schedulersHolder;
    }

    @Override
    public void chooseEveryoneToShare(boolean everyoneChosen, List<DisableableStudentShareDTO> shares) {
        for (DisableableStudentShareDTO dto : shares) {
            dto.setEnabled(!everyoneChosen);
            if (everyoneChosen) {
                dto.setChosen(true);
            }
        }
        view.displayFileShares(shares);
    }

    @Override
    public void loadStudents(String fileId, boolean refresh) {
        addToSubsriptionList(model.loadStudentShares(fileId, refresh)
                .observeOn(schedulersHolder.observ())
                .subscribeOn(schedulersHolder.subscribe())
                .subscribe(studentShareDTOs -> view.displayFileShares(studentShareDTOs),
                        throwable -> view.handleException(throwable)));
    }
}
