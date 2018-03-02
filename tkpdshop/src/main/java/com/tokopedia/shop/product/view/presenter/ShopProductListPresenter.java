package com.tokopedia.shop.product.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.product.domain.interactor.GetShopProductWithWishListUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.AddToWishListUseCase;
import com.tokopedia.wishlist.common.domain.interactor.RemoveFromWishListUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListPresenter extends BaseDaggerPresenter<ShopProductListView> {

    private static final String TAG = "ShopProductListPresente";
    private final GetShopProductWithWishListUseCase getShopProductWithWishListUseCase;
    private final AddToWishListUseCase addToWishListUseCase;
    private final RemoveFromWishListUseCase removeFromWishListUseCase;
    private final UserSession userSession;

    @Inject
    public ShopProductListPresenter(GetShopProductWithWishListUseCase getShopProductWithWishListUseCase,
                                    AddToWishListUseCase addToWishListUseCase,
                                    RemoveFromWishListUseCase removeFromWishListUseCase,
                                    UserSession userSession) {
        this.getShopProductWithWishListUseCase = getShopProductWithWishListUseCase;
        this.addToWishListUseCase = addToWishListUseCase;
        this.removeFromWishListUseCase = removeFromWishListUseCase;
        this.userSession = userSession;
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

    public void addToWishList(final String productId){
        RequestParams requestParam = AddToWishListUseCase.createRequestParam(userSession.getUserId(), productId);
        addToWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorAddToWishList(e);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessAddToWishList(productId, aBoolean);
            }
        });
    }

    public void removeFromWishList(final String productId){
        RequestParams requestParam = AddToWishListUseCase.createRequestParam(userSession.getUserId(), productId);
        removeFromWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorRemoveFromWishList(e);
                }
            }

            @Override
            public void onNext(Boolean value) {
                getView().onSuccessRemoveFromWishList(productId, value);
            }
        });
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