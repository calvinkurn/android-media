package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.product.domain.interactor.GetShopProductFilterUseCase;
import com.tokopedia.shop.product.view.listener.ShopProductFilterListView;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductSortPresenter extends BaseDaggerPresenter<ShopProductFilterListView> {

    private final GetShopProductFilterUseCase getShopProductFilterUseCase;
    private final ShopProductMapper shopProductMapper;

    @Inject
    public ShopProductSortPresenter(GetShopProductFilterUseCase getShopProductFilterUseCase, ShopProductMapper shopProductMapper) {
        this.getShopProductFilterUseCase = getShopProductFilterUseCase;
        this.shopProductMapper = shopProductMapper;
    }

    public void getShopFilterList() {
        getShopProductFilterUseCase.execute(new Subscriber<List<ShopProductSort>>() {
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
            public void onNext(List<ShopProductSort> dataValue) {
                getView().renderList(shopProductMapper.convertSort(dataValue));
            }
        });
    }
}