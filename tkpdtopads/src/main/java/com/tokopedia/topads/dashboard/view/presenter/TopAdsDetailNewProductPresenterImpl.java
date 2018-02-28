package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailNewProductPresenterImpl extends TopAdsDetailEditProductPresenterImpl<TopAdsDetailEditView> implements TopAdsDetailNewProductPresenter {

    TopAdsCreateDetailProductListUseCase topAdsSaveDetailProductListUseCase;

    public TopAdsDetailNewProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                               TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                               TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                               TopAdsProductListUseCase topAdsProductListUseCase, TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase) {
        super(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase, topAdsProductListUseCase, topAdsGetSuggestionUseCase);
        this.topAdsSaveDetailProductListUseCase = topAdsCreateDetailProductListUseCase;
    }

    @Override
    public void saveAd(TopAdsDetailProductViewModel detailAd, ArrayList<TopAdsProductViewModel> topAdsProductList, String source) {
        topAdsSaveDetailProductListUseCase.execute(TopAdsCreateDetailProductListUseCase.createRequestParams(
                TopAdDetailProductMapper.convertViewToDomainList(detailAd, topAdsProductList), source), getSubscriberSaveListAd());
    }

    private Subscriber<TopAdsDetailProductDomainModel> getSubscriberSaveListAd() {
        return new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel topAdsDetailProductDomainModel) {
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(topAdsDetailProductDomainModel));
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsSaveDetailProductListUseCase.unsubscribe();
    }
}