package com.tokopedia.inbox.inboxchat.subscriber;

import rx.Subscriber;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetMessageSubscriber extends Subscriber<String> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(String s) {
        s.equals("");
    }
}
