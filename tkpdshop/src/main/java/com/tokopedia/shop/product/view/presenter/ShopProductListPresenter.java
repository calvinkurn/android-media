package com.tokopedia.shop.product.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.product.domain.interactor.GetShopProductWithWishListUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListPresenter extends BaseDaggerPresenter<BaseListViewListener<ShopProductViewModel>> {

    private final GetShopProductWithWishListUseCase getShopProductWithWishListUseCase;

    @Inject
    public ShopProductListPresenter(GetShopProductWithWishListUseCase getShopProductWithWishListUseCase) {
        this.getShopProductWithWishListUseCase = getShopProductWithWishListUseCase;
    }

    public void getShopPageList(String shopId, String keyword, String etalaseId, int wholesale, int page, int orderBy) {
        ShopProductRequestModel shopProductRequestModel = getShopProductRequestModel(shopId, keyword, etalaseId, wholesale, page, orderBy);
        getShopProductWithWishListUseCase.execute(GetShopProductWithWishListUseCase.createRequestParam(shopProductRequestModel), new Subscriber<PagingList<ShopProductViewModel>>() {
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
            public void onNext(PagingList<ShopProductViewModel> shopProductList) {
                getView().renderList(shopProductList.getList(), PagingListUtils.checkNextPage(shopProductList));
            }
        });
    }

    protected ShopProductRequestModel getShopProductRequestModel(String shopId) {
        return getShopProductRequestModel(shopId, null, null, 0, 1, -1);
    }

    @NonNull
    private static ShopProductRequestModel getShopProductRequestModel(
            String shopId, String keyword, String etalaseId, int wholesale, int page, int orderBy) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel();
        shopProductRequestModel.setShopId(shopId);
        shopProductRequestModel.setPage(page);
        if (etalaseId != null)
            shopProductRequestModel.setEtalaseId(etalaseId);

        if (keyword != null)
            shopProductRequestModel.setKeyword(keyword);

        if (wholesale > 0)
            shopProductRequestModel.setWholesale(wholesale);

        if (orderBy > 0)
            shopProductRequestModel.setOrderBy(orderBy);

        return shopProductRequestModel;
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopProductWithWishListUseCase.unsubscribe();
    }
}