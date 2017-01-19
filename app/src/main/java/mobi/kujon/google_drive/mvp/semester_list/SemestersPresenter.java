package mobi.kujon.google_drive.mvp.semester_list;


import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Subscription;

public class SemestersPresenter implements SemestersMVP.Presenter {

    private SemestersMVP.View view;
    private SemestersMVP.Model model;
    private SchedulersHolder schedulersHolder;
    private Subscription subscription;

    public SemestersPresenter(SemestersMVP.View view, SemestersMVP.Model model, SchedulersHolder schedulersHolder) {
        this.view = view;
        this.model = model;
        this.schedulersHolder = schedulersHolder;
    }

    @Override
    public void askForSemesters(boolean refresh) {
        subscription = model.getListOfSemesters(refresh)
                .subscribeOn(schedulersHolder.subscribe())
                .observeOn(schedulersHolder.observ())
                .subscribe(semesterDTOs -> {
                    view.semesetersLoaded(semesterDTOs);
                }, throwable -> {
                    view.handleException(throwable);
                });
    }

    @Override
    public void clearSubsribtions() {
        if(subscription != null) {
            subscription.unsubscribe();
        }
    }
}
