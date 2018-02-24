package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
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

public class ShopProductListLimitedPresenter extends BaseDaggerPresenter<ShopProductListLimitedView> {

    private final GetShopProductListUseCase getShopProductListUseCase;
    private final GetFeatureProductListUseCase getFeatureProductListUseCase;

    @Inject
    public ShopProductListLimitedPresenter(GetShopProductListUseCase getShopProductListUseCase,
                                           GetFeatureProductListUseCase getFeatureProductListUseCase) {
        this.getShopProductListUseCase = getShopProductListUseCase;
        this.getFeatureProductListUseCase = getFeatureProductListUseCase;
    }

    public void getShopPageList(String shopId) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel();
        shopProductRequestModel.setShopId(shopId);
        shopProductRequestModel.setPage(1);
        getShopProductListUseCase.execute(GetShopProductListUseCase.createRequestParam(shopProductRequestModel), new Subscriber<ShopProductList>() {
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
            public void onNext(ShopProductList shopProductList) {
//                getView().renderList();shopProductList.getList();
            }
        });
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