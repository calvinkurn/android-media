package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.LikeKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class LikeKolPostSubscriber extends Subscriber<LikeKolDomain> {
    private final FeedPlus.View view;

    public LikeKolPostSubscriber(FeedPlus.View view) {
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
    public void onNext(LikeKolDomain likeKolDomain) {
        view.finishLoadingProgress();

    }
}
