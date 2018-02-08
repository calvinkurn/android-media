package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.wishlist.RemoveWishlistDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.WishlistListener;

import rx.Subscriber;

/**
 * @author by nisie on 5/30/17.
 */

public class RemoveWishlistSubscriber extends Subscriber<RemoveWishlistDomain> {

    private WishlistListener viewListener;
    private int adapterPosition;

    public RemoveWishlistSubscriber(WishlistListener viewListener, int adapterPosition) {

        this.viewListener = viewListener;
        this.adapterPosition = adapterPosition;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRemoveWishlist(
                ErrorHandler.getErrorMessage(e), adapterPosition);
    }

    @Override
    public void onNext(RemoveWishlistDomain removeWishlistResult) {
        if (removeWishlistResult.isSuccess())
            viewListener.onSuccessRemoveWishlist(adapterPosition);
        else
            viewListener.onErrorRemoveWishlist(
                    viewListener.getString(R.string.default_request_error_unknown),
                    adapterPosition);
    }
}
