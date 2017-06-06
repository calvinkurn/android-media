package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedResult;

import java.util.ArrayList;

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
        viewListener.onErrorGetFeed();
    }

    @Override
    public void onNext(FeedResult feedResult) {
        ArrayList<Visitable> list = convertToViewModel(feedResult.getDataFeedDomainList());

//        if (list.size() > 0) {
//            if(getCurrentCursor(feedResult) != null){
//                viewListener.updateCursor(getCurrentCursor(feedResult));
//            }else {
//                viewListener.onShowAddFeedMore();
//            }
//            viewListener.onSuccessGetFeed(list);
//        }else{
//            if(feedResult.isHasNext())
//                viewListener.onShowRetryGetFeed();
//            else {
//                viewListener.onShowAddFeedMore();
//            }
//        }

        if (list.size() == 0) {
            viewListener.onShowAddFeedMore();
        }else {
            if (feedResult.isHasNext()) {
                viewListener.updateCursor(getCurrentCursor(feedResult));
                viewListener.onSuccessGetFeed(list);
            } else {
                viewListener.onSuccessGetFeed(list);
                viewListener.onShowAddFeedMore();
            }
        }
    }
}
