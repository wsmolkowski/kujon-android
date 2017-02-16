package mobi.kujon.google_drive.mvp.courses_list;


import mobi.kujon.google_drive.utils.SchedulersHolder;
import rx.Subscription;

public class CoursesPresenter implements CoursesMVP.Presenter {

    private CoursesMVP.Model model;
    private CoursesMVP.View view;
    private SchedulersHolder holder;
    private Subscription subscription;

    public CoursesPresenter(CoursesMVP.Model model, CoursesMVP.View view, SchedulersHolder holder) {
        this.model = model;
        this.view = view;
        this.holder = holder;
    }



    @Override
    public void loadTermsInCourses(boolean refresh) {
        subscription = model.loadCourses(refresh)
                .observeOn(holder.observ())
                .subscribeOn(holder.subscribe())
                .subscribe(courseDTOs -> {
                    view.onCoursesTermsLoaded(courseDTOs);
                }, throwable -> {
                    view.handleException(throwable);
                });
    }

    @Override
    public void clearSubscriptions() {
        if(subscription != null) {
            subscription.unsubscribe();
        }
    }
}
