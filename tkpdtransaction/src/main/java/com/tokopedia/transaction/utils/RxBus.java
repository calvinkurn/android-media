package com.tokopedia.transaction.utils;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * @author Aghny A. Putra on 02/02/18
 */
public class RxBus {

    private static RxBus instance;

    private PublishSubject<Object> subject = PublishSubject.create();

    public static RxBus instanceOf() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    /**
     * Pass events to event listeners
     * @param o Event object
     */
    public void sendEvent(Object o) {
        subject.onNext(o);
    }

    /**
     * Subscribe to this observable
     * @return Observable<Object> object to be subscribed
     */
    public Observable<Object> getEvents() {
        return subject;
    }
}
