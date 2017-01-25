package mobi.kujon.google_drive.mvp.semester_list;


import mobi.kujon.google_drive.mvp.AbstractClearSubsriptions;
import mobi.kujon.google_drive.utils.SchedulersHolder;

public class SemestersPresenter extends AbstractClearSubsriptions implements SemestersMVP.Presenter {

    private SemestersMVP.View view;
    private SemestersMVP.Model model;
    private SchedulersHolder schedulersHolder;

    public SemestersPresenter(SemestersMVP.View view, SemestersMVP.Model model, SchedulersHolder schedulersHolder) {
        this.view = view;
        this.model = model;
        this.schedulersHolder = schedulersHolder;
    }

    @Override
    public void askForSemesters(boolean refresh) {
        addToSubsriptionList(model.getListOfSemesters(refresh)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(semesterDTOs -> {
                    view.semestersLoaded(semesterDTOs);
                }, throwable -> {
                    view.handleException(throwable);
                }));
    }


}
