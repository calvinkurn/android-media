package com.tokopedia.seller.topads.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.utils.ViewUtils;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.view.mapper.TopAdDetailProductMapper;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDetailEditShopPresenterImpl<T extends TopAdsDetailEditView> extends TopAdsGetProductDetailPresenter<T> implements TopAdsDetailEditShopPresenter<T> {

    private TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase;
    private TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase;

    public TopAdsDetailEditShopPresenterImpl(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase,
                                             TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase,
                                             TopAdsProductListUseCase topAdsProductListUseCase) {
        super(topAdsProductListUseCase);
        this.topAdsGetDetailShopUseCase = topAdsGetDetailShopUseCase;
        this.topAdsSaveDetailShopUseCase = topAdsSaveDetailShopUseCase;
    }

    public void saveAd(TopAdsDetailShopViewModel viewModel) {
        topAdsSaveDetailShopUseCase.execute(TopAdsSaveDetailShopUseCase.createRequestParams(TopAdDetailProductMapper.convertViewToDomain(viewModel)),
                getSubscriberSaveShop());
    }

    @NonNull
    protected Subscriber<TopAdsDetailShopDomainModel> getSubscriberSaveShop() {
        return new Subscriber<TopAdsDetailShopDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onSaveAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailShopDomainModel domainModel) {
                getView().onSaveAdSuccess(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        };
    }

    @Override
    public void getDetailAd(String adId) {
        topAdsGetDetailShopUseCase.execute(TopAdsGetDetailShopUseCase.createRequestParams(adId), new Subscriber<TopAdsDetailShopDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadDetailAdError(ViewUtils.getErrorMessage(e));
            }

            @Override
            public void onNext(TopAdsDetailShopDomainModel domainModel) {
                getView().onDetailAdLoaded(TopAdDetailProductMapper.convertDomainToView(domainModel));
            }
        });
    }

    @Override
    public void getProductDetail(String productId) {
        // no op, shop has no product id
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsGetDetailShopUseCase.unsubscribe();
        topAdsSaveDetailShopUseCase.unsubscribe();
    }
}