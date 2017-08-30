package com.tokopedia.seller.topads.dashboard.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.dashboard.domain.model.ProductListDomain;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsGetProductDetailView;
import com.tokopedia.seller.topads.dashboard.view.mapper.TopAdsProductModelMapper;

import rx.Subscriber;

/**
 * Created by Test on 5/31/2017.
 */

public abstract class TopAdsGetProductDetailPresenter<T extends TopAdsGetProductDetailView> extends BaseDaggerPresenter<T> {
    protected TopAdsProductListUseCase topAdsProductListUseCase;

    public TopAdsGetProductDetailPresenter(TopAdsProductListUseCase topAdsProductListUseCase) {
        this.topAdsProductListUseCase = topAdsProductListUseCase;
    }

    public void getProductDetail(String productId) {
        topAdsProductListUseCase.execute(
                TopAdsProductListUseCase.createRequestParamsByProductId(
                        productId), new Subscriber<ProductListDomain>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            // no op
                        }
                    }

                    @Override
                    public void onNext(ProductListDomain productListDomain) {
                        getView().onSuccessLoadTopAdsProduct(TopAdsProductModelMapper.convertModelFromDomainToView(
                                productListDomain.getProductDomains().get(0)));
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsProductListUseCase.unsubscribe();
    }
}
