package com.tokopedia.shop.product.view.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.product.domain.interactor.GetShopProductWithWishListUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.AddToWishListUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListPresenter extends BaseDaggerPresenter<BaseListViewListener<ShopProductViewModel>> {

    private static final String TAG = "ShopProductListPresente";
    private final GetShopProductWithWishListUseCase getShopProductWithWishListUseCase;
    private AddToWishListUseCase addToWishListUseCase;

    @Inject
    public ShopProductListPresenter(GetShopProductWithWishListUseCase getShopProductWithWishListUseCase,
                                    AddToWishListUseCase addToWishListUseCase) {
        this.getShopProductWithWishListUseCase = getShopProductWithWishListUseCase;
        this.addToWishListUseCase = addToWishListUseCase;
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

    public void addToWishList(String shopId, String productId){
        RequestParams requestParam = AddToWishListUseCase.createRequestParam(shopId, productId);
        addToWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.d(TAG, "berhasil wishlist -> "+aBoolean);
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