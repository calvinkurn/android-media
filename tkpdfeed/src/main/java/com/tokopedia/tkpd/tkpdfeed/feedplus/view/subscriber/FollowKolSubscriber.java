package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowKolSubscriber extends Subscriber<FollowKolDomain> {
    private final FeedPlus.View view;

    public FollowKolSubscriber(FeedPlus.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.finishLoadingProgress();
    }

    @Override
    public void onNext(FollowKolDomain followKolDomain) {
        view.finishLoadingProgress();

    }
}
