package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.interactor.GetShopProductFilterUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopPageFeaturedProductUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopPageFeaturedProduct;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListLimitedPresenter extends ShopProductFilterPresenter {

    private final GetFeatureProductListUseCase getFeatureProductListUseCase;
    private final GetShopPageFeaturedProductUseCase getShopPageFeaturedProductUseCase;

    @Inject
    public ShopProductListLimitedPresenter(GetShopProductFilterUseCase getShopProductFilterUseCase,
                                           GetShopProductListUseCase getShopProductListUseCase,
                                           GetFeatureProductListUseCase getFeatureProductListUseCase,
                                          GetShopPageFeaturedProductUseCase getShopPageFeaturedProductUseCase) {
        super(getShopProductFilterUseCase, getShopProductListUseCase);
        this.getFeatureProductListUseCase = getFeatureProductListUseCase;
        this.getShopPageFeaturedProductUseCase = getShopPageFeaturedProductUseCase;
    }

    public void getFeatureProductList(String shopId) {
        getShopPageFeaturedProductUseCase.execute(GetShopPageFeaturedProductUseCase.createRequestParam(shopId), new Subscriber<List<ShopPageFeaturedProduct>>() {
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
            public void onNext(List<ShopPageFeaturedProduct> shopPageFeaturedProductList) {
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
    }
}