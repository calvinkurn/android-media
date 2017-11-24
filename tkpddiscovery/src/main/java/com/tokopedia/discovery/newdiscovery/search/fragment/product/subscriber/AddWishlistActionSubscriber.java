package com.tokopedia.discovery.newdiscovery.search.fragment.product.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.domain.model.ActionResultModel;

import rx.Subscriber;

/**
 * Created by henrypriyono on 10/19/17.
 */

public class AddWishlistActionSubscriber extends Subscriber<ActionResultModel> {
    private final WishlistActionListener viewListener;
    private int adapterPosition;

    public AddWishlistActionSubscriber(WishlistActionListener viewListener, int adapterPosition) {
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
    public void onNext(ActionResultModel result) {
        if (result.isSuccess())
            viewListener.onSuccessAddWishlist(adapterPosition);
        else
            viewListener.onErrorAddWishList(
                    viewListener.getString(R.string.default_request_error_unknown),
                    adapterPosition);
    }
}
