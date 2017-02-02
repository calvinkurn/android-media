package com.tokopedia.seller.common;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Kulomady on 12/7/16.
 */

public interface Interactor<T> {
    void execute(Subscriber<T> subscriber);

    Observable<T> execute();
}
