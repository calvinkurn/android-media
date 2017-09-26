package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ShopFavoritedDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;

import rx.Subscriber;

/**
 * @author by nisie on 9/26/17.
 */

class CheckShopFavoriteSubscriber extends Subscriber<ShopFavoritedDomain> {

    private final InboxReputationDetail.View viewListener;

    public CheckShopFavoriteSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorCheckShopIsFavorited();
    }

    @Override
    public void onNext(ShopFavoritedDomain shopFavoritedDomain) {
        viewListener.onSuccessCheckShopIsFavorited(shopFavoritedDomain.isShopFavorited() ? 1 : 0);
    }
}
