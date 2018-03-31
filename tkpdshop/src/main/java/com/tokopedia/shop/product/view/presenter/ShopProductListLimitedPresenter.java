package com.tokopedia.shop.product.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.product.domain.interactor.GetShopProductLimitedUseCase;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductHomeViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedSearchViewModel;
import com.tokopedia.shop.product.view.model.ShopProductTitleFeaturedViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.AddToWishListUseCase;
import com.tokopedia.wishlist.common.domain.interactor.RemoveFromWishListUseCase;

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
    private static final int FIRST_LOAD = 1;

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

    public boolean isLogin() {
        return userSession.isLoggedIn();
    }

    public String getDeviceId() {
        return userSession.getDeviceId();
    }

    public String getUserId() {
        return userSession.getUserId();
    }

    public void getProductLimitedList(String shopId, boolean goldMerchantStore, boolean officialStore, final String promotionWebViewUrl, final int page) {
        getShopProductLimitedUseCase.execute(GetShopProductLimitedUseCase.createRequestParam(shopId, goldMerchantStore, officialStore, page),
                new Subscriber<PagingList<ShopProductBaseViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.getLocalizedMessage());
                        if (isViewAttached()) {
                            getView().showGetListError(e);
                        }
                    }

                    @Override
                    public void onNext(PagingList<ShopProductBaseViewModel> shopProductBaseViewModelList) {
                        if (page == FIRST_LOAD) {
                            boolean shopHasProduct = shopProductBaseViewModelList.getList().size() > 0;
                            if (!TextApiUtils.isTextEmpty(promotionWebViewUrl)) {
                                shopProductBaseViewModelList.getList().add(0, getProductPromoModel());
                            }
                            // There's at least 1 product, show search view
                            if (shopHasProduct) {
                                shopProductBaseViewModelList.getList().add(0, new ShopProductLimitedSearchViewModel());
                                for(int i = 0; i < shopProductBaseViewModelList.getList().size(); i++){
                                    ShopProductBaseViewModel shopProductBaseViewModel = shopProductBaseViewModelList.getList().get(i);
                                    if(shopProductBaseViewModel instanceof ShopProductHomeViewModel) {
                                        shopProductBaseViewModelList.getList().add(i, new ShopProductLimitedEtalaseTitleViewModel());
                                        break;
                                    }
                                }
                                for(int i = 0; i < shopProductBaseViewModelList.getList().size(); i++){
                                    ShopProductBaseViewModel shopProductBaseViewModel = shopProductBaseViewModelList.getList().get(i);
                                    if(shopProductBaseViewModel instanceof ShopProductLimitedFeaturedViewModel) {
                                        shopProductBaseViewModelList.getList().add(i, new ShopProductTitleFeaturedViewModel());
                                        break;
                                    }
                                }
                            }
                        }
                        getView().renderList(shopProductBaseViewModelList.getList(), PagingListUtils.checkNextPage(shopProductBaseViewModelList));

                    }

                    private ShopProductLimitedPromoViewModel getProductPromoModel() {
                        ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel = new ShopProductLimitedPromoViewModel();
                        shopProductLimitedPromoViewModel.setUserId(userSession.getUserId());
                        shopProductLimitedPromoViewModel.setLogin(userSession.isLoggedIn());
                        String url = promotionWebViewUrl;
                        if (userSession.isLoggedIn()) {
                            url = ShopProductOfficialStoreUtils.getLogInUrl(url, userSession.getDeviceId(), userSession.getUserId());
                        }
                        CommonUtils.dumper(url);
                        shopProductLimitedPromoViewModel.setUrl(url);
                        return shopProductLimitedPromoViewModel;
                    }
                });
    }

    public void addToWishList(final String productId) {
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

    public void removeFromWishList(final String productId) {
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