package com.tokopedia.discovery.newdiscovery.category.presentation.product.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.wishlist.model.AddWishListResponse;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import rx.Subscriber;

/**
 * Created by henrypriyono on 10/19/17.
 */

public class AddWishlistActionSubscriber extends Subscriber<GraphqlResponse> {
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
    public void onNext(GraphqlResponse graphqlResponse) {


        if (graphqlResponse != null) {
            AddWishListResponse addWishListResponse = graphqlResponse.getData(AddWishListResponse.class);
            if (addWishListResponse.getWishlist_add().getSuccess())
                viewListener.onSuccessAddWishlist(adapterPosition);
            else
                viewListener.onErrorAddWishList(
                        viewListener.getString(R.string.default_request_error_unknown),
                        adapterPosition);
        } else {
            viewListener.onErrorAddWishList(
                    viewListener.getString(R.string.default_request_error_unknown),
                    adapterPosition);
        }
    }
}
