package com.tokopedia.discovery.newdiscovery.category.presentation.product.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.domain.model.ActionResultModel;

import rx.Subscriber;

/**
 * Created by henrypriyono on 10/19/17.
 */

public class RemoveWishlistActionSubscriber extends Subscriber<ActionResultModel> {

    private WishlistActionListener viewListener;
    private int adapterPosition;

    public RemoveWishlistActionSubscriber(WishlistActionListener viewListener, int adapterPosition) {

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
    public void onNext(ActionResultModel result) {
        if (result.isSuccess())
            viewListener.onSuccessRemoveWishlist(adapterPosition);
        else
            viewListener.onErrorRemoveWishlist(
                    viewListener.getString(R.string.default_request_error_unknown),
                    adapterPosition);
    }
}
