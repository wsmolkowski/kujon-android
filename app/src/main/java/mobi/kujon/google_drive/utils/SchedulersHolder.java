package mobi.kujon.google_drive.utils;

import rx.schedulers.Schedulers;

/**
 *
 */

public class SchedulersHolder {
    private Schedulers observScheduler,subscribeScheduler;

    public SchedulersHolder(Schedulers observScheduler, Schedulers subscribeScheduler) {
        this.observScheduler = observScheduler;
        this.subscribeScheduler = subscribeScheduler;
    }

    public Schedulers observ() {
        return observScheduler;
    }

    public Schedulers subscribe() {
        return subscribeScheduler;
    }
}
