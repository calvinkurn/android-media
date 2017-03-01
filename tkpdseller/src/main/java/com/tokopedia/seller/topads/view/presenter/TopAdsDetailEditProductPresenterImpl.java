package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditProductPresenterImpl extends TopAdsDetailNewProductPresenterImpl<TopAdsDetailEditView> implements TopAdsDetailEditProductPresenter {

    private TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase;
    private TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase;
    private TopAdsEditPromoFragmentListener listener;
    private Context context;

    public TopAdsDetailEditProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase, TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase) {
        super(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase);
        this.topAdsGetDetailProductUseCase = topAdsGetDetailProductUseCase;
        this.topAdsSaveDetailProductUseCase = topAdsSaveDetailProductUseCase;
    }

    @Override
    public void getDetailAd(String adId) {
        topAdsGetDetailProductUseCase.execute(TopAdsGetDetailProductUseCase.createRequestParams(adId), new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError();
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel domainModel) {
                getView().onDetailAdLoaded(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        });
    }
}