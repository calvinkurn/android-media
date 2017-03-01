package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.exception.ResponseErrorException;
import com.tokopedia.seller.topads.utils.ViewUtils;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailNewView;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewProductPresenterImpl<T extends TopAdsDetailNewView> extends BaseDaggerPresenter<T> implements TopAdsDetailNewProductPresenter<T> {

    private TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase;
    private TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase;
    private TopAdsEditPromoFragmentListener listener;
    private Context context;

    public TopAdsDetailNewProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase, TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase) {
        this.topAdsGetDetailProductUseCase = topAdsGetDetailProductUseCase;
        this.topAdsSaveDetailProductUseCase = topAdsSaveDetailProductUseCase;
    }

    @Override
    public void saveAd(TopAdsDetailProductViewModel adViewModel) {
        topAdsSaveDetailProductUseCase.execute(TopAdsSaveDetailProductUseCase.createRequestParams(TopAdDetailProductMapper.convertViewToDomain(adViewModel)), new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel domainModel) {
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsSaveDetailProductUseCase.unsubscribe();
    }
}