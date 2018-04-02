package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import java.util.ArrayList;

/**
 * @author by nisie on 5/29/17.
 */

public class GetFeedsSubscriber extends GetFirstPageFeedsSubscriber {

    public GetFeedsSubscriber(FeedPlus.View viewListener, int page) {
        super(viewListener, page);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.shouldLoadTopAds(false);
        viewListener.onShowRetryGetFeed();
        viewListener.hideTopAdsAdapterLoading();
    }

    @Override
    public void onNext(FeedResult feedResult) {
        ArrayList<Visitable> list = convertToViewModel(feedResult.getFeedDomain());

        if (list.size() == 0) {
            viewListener.onShowAddFeedMore();
            viewListener.hideTopAdsAdapterLoading();
            viewListener.unsetEndlessScroll();
        }else {
            if (feedResult.isHasNext()) {
                viewListener.updateCursor(getCurrentCursor(feedResult));
                viewListener.onSuccessGetFeed(list);
                viewListener.hideTopAdsAdapterLoading();
            } else {
                viewListener.onSuccessGetFeed(list);
                viewListener.onShowAddFeedMore();
                viewListener.hideTopAdsAdapterLoading();
                viewListener.unsetEndlessScroll();
            }
        }
    }
}
