package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.core.network.entity.affiliateProductData.Affiliate;
import com.tokopedia.core.network.entity.affiliateProductData.AffiliateProductDataResponse;
import com.tokopedia.core.network.entity.wishlistCount.WishlistCountResponse;
import com.tokopedia.tkpdpdp.presenter.ProductDetailPresenter;

import retrofit2.Response;
import rx.Subscriber;

public class AffiliateProductDataSubscriber extends Subscriber<Response<AffiliateProductDataResponse>> {
    private final ProductDetailPresenter productDetailPresenter;

    public AffiliateProductDataSubscriber(ProductDetailPresenter productDetailPresenter){
        this.productDetailPresenter = productDetailPresenter;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Response<AffiliateProductDataResponse> affiliateProductDataResponseResponse) {
        AffiliateProductDataResponse affiliateProductDataResponse =
                affiliateProductDataResponseResponse.body();

        if (affiliateProductDataResponse.getData().getAffiliate().get(0)
                != null){
            Affiliate affiliate = affiliateProductDataResponse.getData().getAffiliate().get(0);
            productDetailPresenter.renderAffiliateButton(affiliate);
        }
    }
}