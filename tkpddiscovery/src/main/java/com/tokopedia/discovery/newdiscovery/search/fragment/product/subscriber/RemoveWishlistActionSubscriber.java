package com.tokopedia.discovery.newdiscovery.search.fragment.product.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.wishlist.model.RemoveWishListResponse;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * Created by henrypriyono on 10/19/17.
 */

public class RemoveWishlistActionSubscriber extends Subscriber<GraphqlResponse> {

    private WishlistActionListener viewListener;
    private String productId;

    public RemoveWishlistActionSubscriber(WishlistActionListener viewListener,
                                          String productId) {

        this.viewListener = viewListener;
        this.productId = productId;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRemoveWishlist(
                ErrorHandler.getErrorMessage(e), productId);
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        if (graphqlResponse != null) {
            RemoveWishListResponse removeWishListResponse = graphqlResponse.getData(RemoveWishListResponse.class);
            if (removeWishListResponse.getWishlistRemove().getSuccess()) {
                viewListener.onSuccessRemoveWishlist(productId);
            } else {
                viewListener.onErrorRemoveWishlist(
                        viewListener.getString(R.string.default_request_error_unknown),
                        productId);
            }

        } else {
            viewListener.onErrorRemoveWishlist(
                    viewListener.getString(R.string.default_request_error_unknown),
                    productId);
        }

    }
}
