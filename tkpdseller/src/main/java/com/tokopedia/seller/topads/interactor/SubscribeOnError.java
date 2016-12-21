package com.tokopedia.seller.topads.interactor;

import rx.functions.Action1;

/**
 * Created by Nathaniel on 11/25/2016.
 */

public class SubscribeOnError implements Action1<Throwable> {

    private DashboardTopadsInteractor.Listener listener;

    public SubscribeOnError(DashboardTopadsInteractor.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void call(Throwable throwable) {
        listener.onError(throwable);
    }
}
