package mobi.kujon.google_drive.mvp;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 *
 */

public class AbstractClearSubsriptions implements ClearSubscriptions {

    private CompositeSubscription compositeSubscription;

    protected void addToSubsriptionList(Subscription subscription) {
        compositeSubscription.add(subscription);
    }


    @Override
    public void clearSubscriptions() {
        compositeSubscription.clear();
    }
}
