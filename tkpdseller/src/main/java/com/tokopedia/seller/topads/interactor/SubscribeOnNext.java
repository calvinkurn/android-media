package com.tokopedia.seller.topads.interactor;

import rx.functions.Action1;

/**
 * Created by Nathaniel on 11/25/2016.
 */

public class SubscribeOnNext<T> implements Action1<T> {

    private DashboardTopadsInteractor.Listener listener;

    public SubscribeOnNext(DashboardTopadsInteractor.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void call(T t) {
        if (t != null) {
            listener.onSuccess(t);
        } else {
            listener.onError(new NullPointerException());
        }
    }
}
