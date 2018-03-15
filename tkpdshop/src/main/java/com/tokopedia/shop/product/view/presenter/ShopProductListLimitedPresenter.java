package com.tokopedia.shop.product.view.presenter;

import android.util.Log;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.product.domain.interactor.GetShopProductLimitedUseCase;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.AddToWishListUseCase;

import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.wishlist.common.domain.interactor.RemoveFromWishListUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListLimitedPresenter extends BaseDaggerPresenter<ShopProductListLimitedView> {

    private final GetShopProductLimitedUseCase getShopProductLimitedUseCase;
    private final AddToWishListUseCase addToWishListUseCase;
    private final RemoveFromWishListUseCase removeFromWishListUseCase;
    private final UserSession userSession;

    private static final String TAG = "ShopProductListLimitedP";

    @Inject
    public ShopProductListLimitedPresenter(GetShopProductLimitedUseCase getShopProductLimitedUseCase,
                                           AddToWishListUseCase addToWishListUseCase,
                                           RemoveFromWishListUseCase removeFromWishListUseCase,
                                           UserSession userSession) {
        this.getShopProductLimitedUseCase = getShopProductLimitedUseCase;
        this.addToWishListUseCase = addToWishListUseCase;
        this.removeFromWishListUseCase = removeFromWishListUseCase;
        this.userSession = userSession;
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }

    public void getProductLimitedList(String shopId, final String promotionWebViewUrl) {
        getShopProductLimitedUseCase.execute(GetShopProductLimitedUseCase.createRequestParam(shopId), new Subscriber<List<ShopProductBaseViewModel>>() {
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
            public void onNext(List<ShopProductBaseViewModel> shopProductBaseViewModelList) {
                if (!TextApiUtils.isTextEmpty(promotionWebViewUrl)) {
                    ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel = new ShopProductLimitedPromoViewModel();
                    shopProductLimitedPromoViewModel.setUserId(userSession.getUserId());
                    shopProductLimitedPromoViewModel.setLogin(userSession.isLoggedIn());
                    String url = promotionWebViewUrl;
                    if (userSession.isLoggedIn()) {
                        url = ShopProductOfficialStoreUtils.getLogInUrl(url, userSession.getDeviceId(), userSession.getUserId());
                    }
                    CommonUtils.dumper(url);
                    shopProductLimitedPromoViewModel.setUrl(url);
                    shopProductBaseViewModelList.add(0, shopProductLimitedPromoViewModel);
                }
                getView().renderList(shopProductBaseViewModelList);
            }
        });
    }

    public void addToWishList(final String productId){
        if (!userSession.isLoggedIn() && isViewAttached()) {
            getView().onErrorAddToWishList(new UserNotLoginException());
            return;
        }
        getView().showLoadingDialog();
        RequestParams requestParam = AddToWishListUseCase.createRequestParam(userSession.getUserId(), productId);
        addToWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorAddToWishList(e);
                    getView().hideLoadingDialog();
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessAddToWishList(productId, aBoolean);
                getView().hideLoadingDialog();
            }
        });
    }

    public void removeFromWishList(final String productId){
        if (!userSession.isLoggedIn() && isViewAttached()) {
            getView().onErrorAddToWishList(new UserNotLoginException());
            return;
        }
        getView().showLoadingDialog();
        RequestParams requestParam = AddToWishListUseCase.createRequestParam(userSession.getUserId(), productId);
        removeFromWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorRemoveFromWishList(e);
                    getView().hideLoadingDialog();
                }
            }

            @Override
            public void onNext(Boolean value) {
                getView().onSuccessRemoveFromWishList(productId, value);
                getView().hideLoadingDialog();
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (getShopProductLimitedUseCase != null) {
            getShopProductLimitedUseCase.unsubscribe();
        }
    }
}