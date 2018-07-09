package com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.domain.model.ActionResultModel;
import com.tokopedia.discovery.newdiscovery.util.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.wishlist.model.AddWishListResponse;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * Created by hangnadi on 10/24/17.
 */

public class AddWishlistActionSubscriber extends Subscriber<GraphqlResponse> {
    private final WishlistActionListener listener;
    private final String productID;

    public AddWishlistActionSubscriber(WishlistActionListener listener, String productID) {
        this.listener = listener;
        this.productID = productID;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        listener.onErrorAddWishList(ErrorHandler.getErrorMessage(e), productID);
    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {

        if (graphqlResponse != null) {
            AddWishListResponse addWishListResponse = graphqlResponse.getData(AddWishListResponse.class);
            if (addWishListResponse.getWishlist_add().getSuccess())
                listener.onSuccessAddWishlist(productID);
            else
                listener.onErrorAddWishList(null, productID);
        } else {
            listener.onErrorAddWishList(null, productID);
        }


        /*if (result.isSuccess()) {
            listener.onSuccessAddWishlist(productID);
        } else {
            listener.onErrorAddWishList(null, productID);
        }*/
    }
}
