package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewProductPresenterImpl extends TopAdsDetailEditProductPresenterImpl<TopAdsDetailEditView> implements TopAdsDetailNewProductPresenter {

    TopAdsCreateDetailProductListUseCase topAdsSaveDetailProductListUseCase;

    public TopAdsDetailNewProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                               TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                               TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase) {
        super(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase);
        this.topAdsSaveDetailProductListUseCase = topAdsCreateDetailProductListUseCase;
    }

    @Override
    public void saveAd(TopAdsDetailProductViewModel detailAd, ArrayList<TopAdsProductViewModel> topAdsProductList) {
        topAdsSaveDetailProductListUseCase.execute(TopAdsCreateDetailProductListUseCase.createRequestParams(
                TopAdDetailProductMapper.convertViewToDomainList(detailAd, topAdsProductList)), getSubscriberSaveListAd());
    }

    private Subscriber<TopAdsDetailProductDomainModel> getSubscriberSaveListAd() {
        return new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(e.getMessage());
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel topAdsDetailProductDomainModel) {
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(topAdsDetailProductDomainModel));
            }
        };
    }
}