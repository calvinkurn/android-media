package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.CheckFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by nisie on 7/7/17.
 */

public class CheckNewFeedSubscriber extends Subscriber<CheckFeedDomain> {

    private final FeedPlus.View viewListener;

    public CheckNewFeedSubscriber(FeedPlus.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(CheckFeedDomain checkFeedDomain) {
        if (checkFeedDomain.getTotalData() > 0) {
            viewListener.onShowNewFeed(checkFeedDomain.getTotalData());
        } else {
            viewListener.onHideNewFeed();
        }
    }
}
