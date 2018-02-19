package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.note.domain.interactor.GetShopNoteListUseCase;
import com.tokopedia.shop.note.view.listener.ShopNoteListView;
import com.tokopedia.shop.note.view.mapper.ShopNoteViewModelMapper;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.mapper.ShopProductViewModelMapper;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListPresenter extends BaseDaggerPresenter<ShopProductListView> {

    private final GetShopProductListUseCase getShopProductListUseCase;
    private final ShopProductViewModelMapper shopProductViewModelMapper;

    @Inject
    public ShopProductListPresenter(GetShopProductListUseCase getShopProductListUseCase, ShopProductViewModelMapper shopProductViewModelMapper) {
        this.getShopProductListUseCase = getShopProductListUseCase;
        this.shopProductViewModelMapper = shopProductViewModelMapper;
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
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(ShopProductList shopProductList) {
                if(!isViewAttached())
                    return;

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