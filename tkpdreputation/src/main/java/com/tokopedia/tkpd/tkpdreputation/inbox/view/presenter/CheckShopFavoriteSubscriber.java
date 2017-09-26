package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.CheckShopFavoriteDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;

import rx.Subscriber;

/**
 * @author by nisie on 9/26/17.
 */

class CheckShopFavoriteSubscriber extends Subscriber<CheckShopFavoriteDomain> {

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
    public void onNext(CheckShopFavoriteDomain checkShopFavoriteDomain) {
        viewListener.onSuccessCheckShopIsFavorited(checkShopFavoriteDomain.isShopFavorited() ? 1 : 0);
    }
}
