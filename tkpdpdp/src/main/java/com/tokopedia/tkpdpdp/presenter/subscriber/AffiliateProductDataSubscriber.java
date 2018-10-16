package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse;
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
        .TopAdsPdpAffiliate.Data.PdpAffiliate;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.viewmodel.AffiliateInfoViewModel;

import rx.Subscriber;

public class AffiliateProductDataSubscriber extends Subscriber<TopAdsPdpAffiliateResponse
        .TopAdsPdpAffiliate> {
    private ProductDetailView viewListener;

    public AffiliateProductDataSubscriber(ProductDetailView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate topAdsPdpAffiliate) {
        if (topAdsPdpAffiliate != null
                && topAdsPdpAffiliate.getData() != null
                && topAdsPdpAffiliate.getData().getAffiliate() != null
                && !topAdsPdpAffiliate.getData().getAffiliate().isEmpty()) {
            PdpAffiliate entityModel = topAdsPdpAffiliate.getData().getAffiliate().get(0);

            if (entityModel.getAdId() != 0 && entityModel.getProductId() != 0) {
                AffiliateInfoViewModel viewModel = new AffiliateInfoViewModel(
                        entityModel.getAdId(),
                        entityModel.getCommissionPercent(),
                        entityModel.getCommissionPercentDispay(),
                        entityModel.getCommissionValue(),
                        entityModel.getCommissionValueDisplay(),
                        entityModel.getProductId(),
                        entityModel.getUniqueURL()
                );
                viewListener.renderAffiliateButton(viewModel);
            }
        }
    }

//    private final ProductDetailPresenter productDetailPresenter;

//    public AffiliateProductDataSubscriber(ProductDetailPresenter productDetailPresenter){
//        this.productDetailPresenter = productDetailPresenter;
//    }
//    @Override
//    public void onCompleted() {
//
//    }
//
//    @Override
//    public void onError(Throwable e) {
//
//    }
//
//    @Override
//    public void onNext(Response<AffiliateProductDataResponse>
// affiliateProductDataResponseResponse) {
//        AffiliateProductDataResponse affiliateProductDataResponse =
//                affiliateProductDataResponseResponse.body();
//
//        if (affiliateProductDataResponse.getData().getAffiliate().get(0)
//                != null){
//            Affiliate affiliate = affiliateProductDataResponse.getData().getAffiliate().get(0);
//            productDetailPresenter.renderAffiliateButton(affiliate);
//        }
//    }
}