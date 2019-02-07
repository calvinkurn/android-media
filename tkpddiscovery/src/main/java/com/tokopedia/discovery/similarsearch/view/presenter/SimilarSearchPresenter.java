package com.tokopedia.discovery.similarsearch.view.presenter;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.similarsearch.analytics.SimilarSearchTracking;
import com.tokopedia.discovery.similarsearch.domain.GetSimilarProductUseCase;
import com.tokopedia.discovery.similarsearch.model.ProductsItem;
import com.tokopedia.discovery.similarsearch.view.SimilarSearchContract;
import com.tokopedia.discovery.similarsearch.view.SimilarSearchFragment;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class SimilarSearchPresenter extends BaseDaggerPresenter<SimilarSearchContract.View> implements SimilarSearchContract.Presenter{


    AddWishListUseCase addWishlistActionUseCase;
    RemoveWishListUseCase removeWishlistActionUseCase;
    GetSimilarProductUseCase getSimilarProductUseCase;

    private WishListActionListener wishListListener;


    @Inject
    public SimilarSearchPresenter(AddWishListUseCase addWishlistActionUseCase, RemoveWishListUseCase removeWishlistActionUseCase, GetSimilarProductUseCase getSimilarProductUseCase) {
        this.addWishlistActionUseCase = addWishlistActionUseCase;
        this.removeWishlistActionUseCase = removeWishlistActionUseCase;
        this.getSimilarProductUseCase = getSimilarProductUseCase;
    }

    @Override
    public void attachView(SimilarSearchContract.View view) {
        super.attachView(view);
        List<ProductsItem> productList = new ArrayList<>();
        productList.add(new ProductsItem());
        productList.add(new ProductsItem());
        productList.add(new ProductsItem());
        productList.add(new ProductsItem());
        productList.add(new ProductsItem());
        getView().setProductList(productList);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("product_id", getView().getProductID());
        requestParams.putString("params", "page=0&page_size=100&device=android");
        requestParams.putString("userId",getView().getUserId());

        getSimilarProductUseCase.execute(requestParams, new Subscriber<List<ProductsItem>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                SimilarSearchTracking.eventUserSeeNoSimilarProduct(getView().getAppContext(), getView().getProductID(),getView().getScreenName());
                getView().setEmptyLayoutVisible();
                getView().setContentLayoutGone();
            }

            @Override
            public void onNext(List<ProductsItem> productsItems) {
                if(productsItems != null && productsItems.size() > 0) {
                    getView().setProductList(productsItems);
                } else {
                    getView().setEmptyLayoutVisible();
                    SimilarSearchTracking.eventUserSeeNoSimilarProduct((getView().getAppContext()), getView().getProductID(),getView().getScreenName());
                    getView().setContentLayoutGone();
                }
            }
        });
    }

    @Override
    public void handleWishlistButtonClicked(ProductsItem productItem, int adapterPosition) {
        if (getView().isUserHasLogin()) {
            getView().disableWishlistButton(adapterPosition);
            if (productItem.isWishListed()) {
                removeWishlist(String.valueOf(productItem.getId()), getView().getUserId(), adapterPosition);
            } else {
                addWishlist(String.valueOf(productItem.getId()), getView().getUserId(), adapterPosition);
            }
        } else {
            launchLoginActivity(String.valueOf(productItem.getId()));
        }
    }

    private void launchLoginActivity(String productId) {
        Bundle extras = new Bundle();
        extras.putString("product_id", productId);
        getView().launchLoginActivity(extras);
    }

    private void addWishlist(String productId, String userId, int adapterPosition) {
        SimilarSearchTracking.eventAddWishList(getView().getAppContext(), productId);
        addWishlistActionUseCase.createObservable(productId, userId,
                wishListListener);
    }


    private void removeWishlist(String productId, String userId, int adapterPosition) {
        SimilarSearchTracking.eventRemoveWishList(getView().getAppContext(), productId);
        removeWishlistActionUseCase.createObservable(productId,
                userId, wishListListener);
    }

    public void setWishListListener(SimilarSearchFragment wishListListener) {
        this.wishListListener = wishListListener;
    }
}
