package com.tokopedia.seller.topads.dashboard.view.presenter;

import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.seller.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.dashboard.utils.ViewUtils;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.seller.topads.dashboard.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailProductViewModel;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditProductPresenterImpl<T extends TopAdsDetailEditView> extends TopAdsGetProductDetailPresenter<T> implements TopAdsDetailEditProductPresenter<T> {

    private TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase;
    private TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase;
    private TopAdsEditPromoFragmentListener listener;

    public TopAdsDetailEditProductPresenterImpl(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                                TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                                TopAdsProductListUseCase topAdsProductListUseCase) {
        super(topAdsProductListUseCase);
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
    public void getDetailAd(String adId) {
        topAdsGetDetailProductUseCase.execute(TopAdsGetDetailProductUseCase.createRequestParams(adId), new Subscriber<TopAdsDetailProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailProductDomainModel domainModel) {
                getView().onDetailAdLoaded(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetDetailProductUseCase.unsubscribe();
        topAdsSaveDetailProductUseCase.unsubscribe();
    }
}