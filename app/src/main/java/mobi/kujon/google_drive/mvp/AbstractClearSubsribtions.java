package mobi.kujon.google_drive.mvp;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 *
 */

public class AbstractClearSubsribtions implements ClearSubscribtions {

    private CompositeSubscription compositeSubscription;

    protected void addToSubsribtionList(Subscription subscription) {
        compositeSubscription.add(subscription);
    }


    @Override
    public void clearSubsribtions() {
        compositeSubscription.clear();
    }
}
