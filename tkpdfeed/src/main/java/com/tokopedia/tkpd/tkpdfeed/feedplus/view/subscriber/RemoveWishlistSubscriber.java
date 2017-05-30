package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.wishlist.RemoveWishlistDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;

import rx.Subscriber;

/**
 * @author by nisie on 5/30/17.
 */

public class RemoveWishlistSubscriber extends Subscriber<RemoveWishlistDomain> {

    public RemoveWishlistSubscriber(FeedPlusDetail.View viewListener) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(RemoveWishlistDomain removeWishlistResult) {

    }
}
