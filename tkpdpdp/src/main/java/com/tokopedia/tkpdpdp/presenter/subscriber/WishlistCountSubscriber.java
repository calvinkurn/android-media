package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.core.network.entity.wishlistCount.WishlistCountResponse;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import retrofit2.Response;
import rx.Subscriber;

public class WishlistCountSubscriber extends Subscriber<Response<WishlistCountResponse>> {
    private final ProductDetailView viewListener;

    public WishlistCountSubscriber(ProductDetailView viewListener){
        this.viewListener = viewListener;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onWishlistCountLoaded("-");
    }

    @Override
    public void onNext(Response<WishlistCountResponse> wishlistCountResponseResponse) {
        String wishlistCountText = "0";

        if(wishlistCountResponseResponse.body().getData() != null){
            int wishlistCount = wishlistCountResponseResponse.body().getData().getCount();
            wishlistCountText = String.valueOf(wishlistCount);
        }

        viewListener.onWishlistCountLoaded(wishlistCountText);
    }
}