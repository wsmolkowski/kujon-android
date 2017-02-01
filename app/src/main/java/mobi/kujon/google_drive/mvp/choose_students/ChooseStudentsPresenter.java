package mobi.kujon.google_drive.mvp.choose_students;

import mobi.kujon.google_drive.model.dto.file_share.AskForStudentDto;
import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

/**
 *
 */

public class ChooseStudentsPresenter extends AbstractClearSubsriptions implements ChooseStudentsMVP.Presenter {
    private ChooseStudentsMVP.Model model;
    private ChooseStudentsMVP.View view;
    private SchedulersHolder schedulersHolder;


    public ChooseStudentsPresenter(ChooseStudentsMVP.Model model, ChooseStudentsMVP.View view, SchedulersHolder schedulersHolder) {
        this.model = model;
        this.view = view;
        this.schedulersHolder = schedulersHolder;
    }

    @Override
    public void loadListOfStudents(AskForStudentDto askForStudentDto, boolean refresh) {
        addToSubsriptionList(
                model.provideListOfStudents(askForStudentDto.getCourseId(), askForStudentDto.getTermId(), refresh)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(studentShareDtos -> {
                    view.showStudentList(studentShareDtos);
                }, throwable -> view.handleException(throwable)));
    }
}
