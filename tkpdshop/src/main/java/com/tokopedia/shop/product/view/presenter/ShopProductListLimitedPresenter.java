package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.interactor.GetShopProductFilterUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.listener.ShopProductListView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListLimitedPresenter extends ShopProductFilterPresenter {

    private final GetFeatureProductListUseCase getFeatureProductListUseCase;

    @Inject
    public ShopProductListLimitedPresenter(GetShopProductFilterUseCase getShopProductFilterUseCase,
                                           GetShopProductListUseCase getShopProductListUseCase,
                                           GetFeatureProductListUseCase getFeatureProductListUseCase) {
        super(getShopProductFilterUseCase, getShopProductListUseCase);
        this.getFeatureProductListUseCase = getFeatureProductListUseCase;
    }

    public void getFeatureProductList(String shopId) {
        getFeatureProductListUseCase.execute(GetFeatureProductListUseCase.createRequestParam(shopId), new Subscriber<List<GMFeaturedProduct>>() {
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
            public void onNext(List<GMFeaturedProduct> featuredProductList) {
//                getView().renderList();shopProductList.getList();
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopProductListUseCase.unsubscribe();
    }
}