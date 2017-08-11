package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by nisie on 7/7/17.
 */

public class CheckNewFeedSubscriber extends Subscriber<FeedResult> {

    private final FeedPlus.View viewListener;
    private final String firstCursor;

    public CheckNewFeedSubscriber(String firstCursor, FeedPlus.View viewListener) {
        this.viewListener = viewListener;
        this.firstCursor = firstCursor;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(FeedResult feedResult) {
        FeedDomain feedDomain = feedResult.getFeedDomain();

        if(feedDomain.getListFeed()!= null
                && !feedDomain.getListFeed().isEmpty()
                && !feedDomain.getListFeed().get(0).getCursor().equals(firstCursor)){
            viewListener.onShowNewFeed();
        }else{
            viewListener.onHideNewFeed();

        }
    }
}
