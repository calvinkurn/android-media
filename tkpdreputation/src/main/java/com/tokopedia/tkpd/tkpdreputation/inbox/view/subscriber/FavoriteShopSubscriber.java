package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.FavoriteShopDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;

import rx.Subscriber;

/**
 * @author by nisie on 9/26/17.
 */

public class FavoriteShopSubscriber extends Subscriber<FavoriteShopDomain> {
    private final InboxReputationDetail.View viewListener;

    public FavoriteShopSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingDialog();
        viewListener.onErrorFavoriteShop(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(FavoriteShopDomain favoriteShopDomain) {
        viewListener.finishLoadingDialog();

        if (favoriteShopDomain.getIsSuccess() == 1)
            viewListener.onSuccessFavoriteShop();
        else
            viewListener.onErrorFavoriteShop(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown));

    }
}
