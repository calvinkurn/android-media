package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.exception.ResponseErrorException;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditShopPresenterImpl extends TopAdsDetailEditPresenterImpl implements TopAdsDetailEditShopPresenter {

    private TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase;
    private TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase;
    private TopAdsEditPromoFragmentListener listener;
    private Context context;

    public TopAdsDetailEditShopPresenterImpl(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase, TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase) {
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
            public void onNext(TopAdsDetailShopDomainModel domainModel) {
                getView().onDetailAdLoaded(TopAdDetailProductMapper.convertDomainToView(domainModel));
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
                if (e instanceof ResponseErrorException) {
                    Log.e("Test", ((ResponseErrorException) e).getErrorList().get(0).getDetail());
                }
                getView().onSaveAdError();
            }

            @Override
            public void onNext(TopAdsDetailShopDomainModel domainModel) {
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        });
    }
}