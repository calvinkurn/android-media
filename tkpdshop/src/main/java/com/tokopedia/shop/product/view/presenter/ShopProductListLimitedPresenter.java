package com.tokopedia.shop.product.view.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.product.domain.interactor.GetShopProductFeaturedUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductLimitedUseCase;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.AddToWishListUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListLimitedPresenter extends BaseDaggerPresenter<ShopProductListLimitedView> {

    private final GetShopProductLimitedUseCase getShopProductLimitedUseCase;
    private AddToWishListUseCase addToWishListUseCase;

    private static final String TAG = "ShopProductListLimitedP";

    @Inject
    public ShopProductListLimitedPresenter(GetShopProductLimitedUseCase getShopProductLimitedUseCase,
                                           AddToWishListUseCase addToWishListUseCase) {
        this.getShopProductLimitedUseCase = getShopProductLimitedUseCase;
        this.addToWishListUseCase = addToWishListUseCase;
    }

    public void addToWishList(String shopId, String productId){
        RequestParams requestParam = AddToWishListUseCase.createRequestParam(shopId, productId);
        addToWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
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
            public void onNext(Boolean aBoolean) {
                Log.d(TAG, "berhasil wishlist -> "+aBoolean);
            }
        });
    }

    public void getProductLimitedList(String shopId) {
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
                getView().renderList(shopProductBaseViewModelList);
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