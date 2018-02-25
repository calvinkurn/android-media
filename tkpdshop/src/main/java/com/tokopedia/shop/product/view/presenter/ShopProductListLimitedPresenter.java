package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.product.domain.interactor.GetShopProductFeaturedUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductLimitedUseCase;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListLimitedPresenter extends BaseDaggerPresenter<ShopProductListLimitedView> {

    private final GetShopProductLimitedUseCase getShopProductLimitedUseCase;

    @Inject
    public ShopProductListLimitedPresenter(GetShopProductLimitedUseCase getShopProductLimitedUseCase) {
        this.getShopProductLimitedUseCase = getShopProductLimitedUseCase;
    }

    public void getProductLimitedList(String shopId) {
        getShopProductLimitedUseCase.execute(GetShopProductLimitedUseCase.createRequestParam(shopId), new Subscriber<List<ShopProductBaseViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<ShopProductBaseViewModel> shopProductBaseViewModelList) {
                getView().renderList(shopProductBaseViewModelList);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (getShopProductLimitedUseCase != null) {
            getShopProductLimitedUseCase.unsubscribe();
        }
    }
}