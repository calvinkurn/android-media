package com.tokopedia.shop.sort.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.sort.domain.interactor.GetShopProductSortUseCase;
import com.tokopedia.shop.sort.view.listener.ShopProductSortListView;
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class ShopProductSortPresenter extends BaseDaggerPresenter<ShopProductSortListView> {

    private final GetShopProductSortUseCase getShopProductFilterUseCase;
    private final ShopProductSortMapper shopProductFilterMapper;

    @Inject
    public ShopProductSortPresenter(GetShopProductSortUseCase getShopProductFilterUseCase, ShopProductSortMapper shopProductMapper) {
        this.getShopProductFilterUseCase = getShopProductFilterUseCase;
        this.shopProductFilterMapper = shopProductMapper;
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
                getView().renderList(shopProductFilterMapper.convertSort(dataValue));
            }
        });
    }
}