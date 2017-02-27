package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsEditPromoShopPresenterImpl extends TopAdsEditPromoPresenterImpl implements TopAdsEditPromoShopPresenter {

    private TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase;
    private TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase;
    private TopAdsEditPromoFragmentListener listener;
    private Context context;

    public TopAdsEditPromoShopPresenterImpl(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase, TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase) {
        this.topAdsGetDetailShopUseCase = topAdsGetDetailShopUseCase;
        this.topAdsSaveDetailShopUseCase = topAdsSaveDetailShopUseCase;
    }

    @Override
    public void getDetailAd(String adId) {
        topAdsGetDetailShopUseCase.execute(TopAdsGetDetailShopUseCase.createRequestParams(adId), new Subscriber<TopAdsDetailShopDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError();
            }

            @Override
            public void onNext(TopAdsDetailShopDomainModel topAdsShopDetailDomainModel) {
                getView().onDetailAdLoaded(TopAdDetailProductMapper.convertDomainToView(topAdsShopDetailDomainModel));
            }
        });
    }

    public void saveAd(TopAdsDetailShopViewModel adViewModel) {
        topAdsSaveDetailShopUseCase.execute(TopAdsSaveDetailShopUseCase.createRequestParams(TopAdDetailProductMapper.convertViewToDomain(adViewModel)), new Subscriber<TopAdsDetailShopDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel) {

            }
        });
    }
}