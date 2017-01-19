package mobi.kujon.google_drive.network;

import mobi.kujon.network.json.KujonResponse;
import rx.Observable;
import rx.exceptions.Exceptions;

/**
 *
 */

public class BackendWrapper<T> {

    public Observable<T> doSmething(Observable<KujonResponse<T>> observable){
        return observable.map(it-> {
            if(it.code == 200)   return it.data;
             else{
                throw Exceptions.propagate(new KujonException(it.message));
            }
        });
    }
}
