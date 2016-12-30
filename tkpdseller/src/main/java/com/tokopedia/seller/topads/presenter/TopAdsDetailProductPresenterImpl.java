package com.tokopedia.seller.topads.presenter;

import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.response.PageDataResponse;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

import java.util.List;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public class TopAdsDetailProductPresenterImpl extends TopAdsDetailPresenterImpl implements TopAdsDetailProductPresenter {

    private TopAdsProductAdInteractor topAdsProductAdInteractor;

    public TopAdsDetailProductPresenterImpl(TopAdsDetailViewListener topAdsDetailViewListener) {
        super(topAdsDetailViewListener);
        topAdsProductAdInteractor = new TopAdsProductAdInteractorImpl();
    }

    @Override
    public void getDetailFromNet(String topAdsId) {
        topAdsProductAdInteractor.getDetailProductAd(new ListenerInteractor<List<ProductAd>>() {

            @Override
            public void onSuccess(List<ProductAd> productAds) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }
}
