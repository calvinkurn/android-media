package com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.discovery.newdiscovery.domain.model.ActionResultModel;
import com.tokopedia.discovery.newdiscovery.util.WishlistActionListener;

import rx.Subscriber;

/**
 * Created by hangnadi on 10/24/17.
 */

public class RemoveWishlistActionSubscriber extends Subscriber<ActionResultModel> {
    private final String productID;
    private final WishlistActionListener listener;

    public RemoveWishlistActionSubscriber(WishlistActionListener listener, String productID) {
        this.listener = listener;
        this.productID = productID;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        listener.onErrorRemoveWishlist(
                ErrorHandler.getErrorMessage(e), productID);
    }

    @Override
    public void onNext(ActionResultModel result) {
        if (result.isSuccess()) {
            listener.onSuccessRemoveWishlist(productID);
        } else {
            listener.onErrorRemoveWishlist(null, productID);
        }
    }
}
