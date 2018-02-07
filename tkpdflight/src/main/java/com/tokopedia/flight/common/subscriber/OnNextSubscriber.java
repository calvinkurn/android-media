package com.tokopedia.flight.common.subscriber;

import rx.Subscriber;

/**
 * Created by User on 11/20/2017.
 */

public abstract class OnNextSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public abstract void onNext(T t);
}
