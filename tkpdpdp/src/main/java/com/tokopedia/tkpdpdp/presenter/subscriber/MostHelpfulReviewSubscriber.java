package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.core.network.entity.wishlistCount.WishlistCountResponse;
import com.tokopedia.core.product.model.productdetail.mosthelpful.MostHelpfulReviewResponse;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.List;

import retrofit2.Response;
import rx.Subscriber;

public class MostHelpfulReviewSubscriber extends Subscriber<List<Review>> {
    private final ProductDetailView viewListener;

    public MostHelpfulReviewSubscriber(ProductDetailView viewListener){
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(List<Review> mostHelpfulReview) {
        if (mostHelpfulReview != null && mostHelpfulReview.size() > 0)
            viewListener.showMostHelpfulReview(mostHelpfulReview);
    }
}