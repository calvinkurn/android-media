package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.exception.ResponseErrorException;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailNewView;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewShopPresenterImpl<T extends TopAdsDetailNewView> extends BaseDaggerPresenter<T> implements TopAdsDetailNewShopPresenter<T> {

    private TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase;
    private TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase;
    private TopAdsEditPromoFragmentListener listener;
    private Context context;

    public TopAdsDetailNewShopPresenterImpl(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase, TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase) {
        this.topAdsGetDetailShopUseCase = topAdsGetDetailShopUseCase;
        this.topAdsSaveDetailShopUseCase = topAdsSaveDetailShopUseCase;
    }

    public void saveAd(TopAdsDetailShopViewModel viewModel) {
        topAdsSaveDetailShopUseCase.execute(TopAdsSaveDetailShopUseCase.createRequestParams(TopAdDetailProductMapper.convertViewToDomain(viewModel)), new Subscriber<TopAdsDetailShopDomainModel>() {
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