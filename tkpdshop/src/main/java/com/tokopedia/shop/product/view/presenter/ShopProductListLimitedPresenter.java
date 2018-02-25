package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.product.domain.interactor.GetShopPageFeaturedProductUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListUseCase;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListLimitedPresenter extends BaseDaggerPresenter<ShopProductListLimitedView> {

    private final GetShopPageFeaturedProductUseCase getShopPageFeaturedProductUseCase;
    private final GetShopProductListUseCase getShopProductListUseCase;

    @Inject
    public ShopProductListLimitedPresenter(
            GetShopPageFeaturedProductUseCase getShopPageFeaturedProductUseCase,
            GetShopProductListUseCase getShopProductListUseCase) {
        this.getShopPageFeaturedProductUseCase = getShopPageFeaturedProductUseCase;
        this.getShopProductListUseCase = getShopProductListUseCase;
    }

    public void getFeatureProductList(String shopId) {
        getShopPageFeaturedProductUseCase.execute(GetShopPageFeaturedProductUseCase.createRequestParam(shopId), new Subscriber<List<ShopProductFeaturedViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
//                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<ShopProductFeaturedViewModel> shopPageFeaturedProductList) {
//                getView().renderList();shopProductList.getList();
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (getShopProductListUseCase != null) {
            getShopProductListUseCase.unsubscribe();
        }
        if (getShopProductListUseCase != null) {
            getShopProductListUseCase.unsubscribe();
        }
    }
}