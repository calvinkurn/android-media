package com.tokopedia.usecase;

import rx.Observable;
import rx.Subscriber;

/**
 * @author Kulomady on 12/7/16.
 */

public interface Interactor<T> {

    void execute(RequestParams requestParams, Subscriber<T> subscriber);

    Observable<T> getExecuteObservable(RequestParams requestParams);
}
