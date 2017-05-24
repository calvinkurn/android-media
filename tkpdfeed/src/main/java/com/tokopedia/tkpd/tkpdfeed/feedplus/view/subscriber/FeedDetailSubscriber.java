package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail.DataFeedDetailDomain;

import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailSubscriber extends Subscriber<List<DataFeedDetailDomain>> {
    private final FeedPlusDetail.View viewListener;

    public FeedDetailSubscriber(FeedPlusDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetFeedDetail(e.toString());
    }

    @Override
    public void onNext(List<DataFeedDetailDomain> dataFeedDetailDomains) {
        viewListener.onGetFeedDetail();

    }
}
