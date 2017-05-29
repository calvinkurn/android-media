package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;

/**
 * @author by nisie on 5/29/17.
 */

public class GetFeedsSubscriber extends GetFirstPageFeedsSubscriber {

    public GetFeedsSubscriber(FeedPlus.View viewListener) {
        super(viewListener);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(FeedResult feedResult) {
        if (feedResult.getDataSource() == FeedResult.SOURCE_LOCAL) {
            viewListener.onSuccessGetFeed(
                    convertToViewModel(feedResult.getDataFeedDomainList()));
        } else {
            viewListener.onSuccessGetFeed(
                    convertToViewModel(feedResult.getDataFeedDomainList()));
        }

        if (feedResult.getDataFeedDomainList().size() > 0)
            viewListener.updateCursor(getCurrentCursor(feedResult));
    }
}
