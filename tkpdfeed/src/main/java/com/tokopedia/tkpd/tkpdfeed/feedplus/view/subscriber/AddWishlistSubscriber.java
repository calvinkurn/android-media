package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.wishlist.AddWishlistDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;

import rx.Subscriber;

/**
 * @author by nisie on 5/30/17.
 */

public class AddWishlistSubscriber extends Subscriber<AddWishlistDomain> {

    private final FeedPlusDetail.View viewListener;

    public AddWishlistSubscriber(FeedPlusDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(AddWishlistDomain addWishlistDomain) {

    }
}
