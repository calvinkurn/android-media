package com.tokopedia.usecase;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author Kulomady on 2/1/17.
 */

public abstract class UseCase<T> implements Interactor<T> {

    protected Subscription subscription = Subscriptions.empty();
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public UseCase() {
    }

    public abstract Observable<T> createObservable(RequestParams requestParams);

    public final T getData(RequestParams requestParams) {
        return createObservable(requestParams).defaultIfEmpty(null).toBlocking().first();
    }

    @Override
    public final void execute(RequestParams requestParams, Subscriber<T> subscriber) {
        execute(requestParams, subscriber, false);
    }

    public final void executeSync(RequestParams requestParams) {
        execute(requestParams, null, true);
    }

    public final void executeSync(RequestParams requestParams, Subscriber<T> subscriber) {
        execute(requestParams, subscriber, true);
    }

    private void execute(RequestParams requestParams, Subscriber<T> subscriber, boolean sync) {
        try {
            if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
                compositeSubscription = new CompositeSubscription();
            }
            Observable<T> observable;
            if (sync) {
                observable = Observable.just(createObservable(requestParams)
                        .defaultIfEmpty(null).toBlocking().first())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            } else {
                observable = createObservable(requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
            if (subscriber != null) {
                subscription = observable.subscribe(subscriber);
                compositeSubscription.add(subscription);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void unsubscribe() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public Observable<T> getExecuteObservable(RequestParams requestParams) {
        return createObservable(requestParams);
    }
}