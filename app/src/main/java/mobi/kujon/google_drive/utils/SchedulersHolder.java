package mobi.kujon.google_drive.utils;


import rx.Scheduler;

/**
 *
 */

public class SchedulersHolder {
    private Scheduler observScheduler,subscribeScheduler;

    public SchedulersHolder(Scheduler observScheduler, Scheduler subscribeScheduler) {
        this.observScheduler = observScheduler;
        this.subscribeScheduler = subscribeScheduler;
    }

    public Scheduler observ() {
        return observScheduler;
    }

    public Scheduler subscribe() {
        return subscribeScheduler;
    }
}
