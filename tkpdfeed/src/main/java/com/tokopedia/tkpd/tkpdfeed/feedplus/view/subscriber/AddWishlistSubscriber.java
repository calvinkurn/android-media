package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.wishlist.AddWishlistDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.WishlistListener;

import rx.Subscriber;

/**
 * @author by nisie on 5/30/17.
 */

public class AddWishlistSubscriber extends Subscriber<AddWishlistDomain> {

    private final WishlistListener viewListener;
    private int adapterPosition;

    public AddWishlistSubscriber(WishlistListener viewListener, int adapterPosition) {
        this.viewListener = viewListener;
        this.adapterPosition = adapterPosition;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorAddWishList(
                ErrorHandler.getErrorMessage(e), adapterPosition);
    }

    @Override
    public void onNext(AddWishlistDomain addWishlistDomain) {
        if (addWishlistDomain.isSuccess())
            viewListener.onSuccessAddWishlist(adapterPosition);
        else
            viewListener.onErrorAddWishList(
                    viewListener.getString(R.string.default_request_error_unknown),
                    adapterPosition);
    }
}
