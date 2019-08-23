package com.tokopedia.discovery.imagesearch.search.fragment.product;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.ProductWishlistUrlUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sachinbansal on 4/13/18.
 */

public class ImageProductListPresenterImpl extends BaseDaggerPresenter<ImageProductListFragmentView> implements ImageProductListPresenter {

    private static final int ITEM_COUNT_PER_PAGE = 12;
    private static final long LOAD_MORE_DELAY_MS = 1000;
    @Inject
    ProductWishlistUrlUseCase wishlistUrlUseCase;
    @Inject
    GetProductUseCase getProductUseCase;
    @Inject
    AddWishListUseCase addWishlistActionUseCase;
    @Inject
    RemoveWishListUseCase removeWishlistActionUseCase;
    private WishListActionListener wishListActionListener;
    private Context context;
    private List<Visitable> dataList = new ArrayList<>();

    public ImageProductListPresenterImpl(Context context) {
        this.context = context;
        SearchComponent component = DaggerSearchComponent.builder()
                .appComponent(getComponent(context))
                .build();
        component.inject(this);
    }

    @Override
    public void attachView(ImageProductListFragmentView viewListener,
                           WishListActionListener wishlistActionListener) {
        super.attachView(viewListener);
        this.wishListActionListener = wishlistActionListener;
    }

    @Override
    public void initData(List<Visitable> data) {
        dataList.clear();
        dataList.addAll(data);
    }

    @Override
    public void loadMoreData(int page) {
        List<Visitable> responseList = new ArrayList<>();

        int fromIndex = page * ITEM_COUNT_PER_PAGE;
        int toIndex = fromIndex + ITEM_COUNT_PER_PAGE;
        toIndex = toIndex > dataList.size() ? dataList.size() : toIndex;

        if (fromIndex < dataList.size()) {
            responseList.addAll(dataList.subList(fromIndex, toIndex));
        }

        Observable.just(responseList)
                .delay(LOAD_MORE_DELAY_MS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Visitable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Visitable> visitables) {
                        if (!visitables.isEmpty()) {
                            getView().appendProductList(visitables);
                        } else {
                            getView().unSetTopAdsEndlessListener();
                        }
                    }
                });
    }

    @Override
    public void handleWishlistButtonClicked(ProductItem productItem) {
        if (getView().isUserHasLogin()) {
            getView().disableWishlistButton(productItem.getProductID());
            if (productItem.isTopAds()) {
                com.tokopedia.usecase.RequestParams params = com.tokopedia.usecase.RequestParams.create();
                params.putString(ProductWishlistUrlUseCase.PRODUCT_WISHLIST_URL,
                        productItem.getProductWishlistUrl());
                wishlistUrlUseCase.execute(params, getWishlistSubscriber(productItem));
            } else {
                if (productItem.isWishlisted()) {
                    removeWishlist(productItem.getProductID(), getView().getUserId());
                } else {
                    addWishlist(productItem.getProductID(), getView().getUserId());
                }
            }
        } else {
            launchLoginActivity(productItem.getProductID());
        }
    }

    @NonNull
    protected Subscriber<Boolean> getWishlistSubscriber(final ProductItem productItem) {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    wishListActionListener.onErrorRemoveWishlist(context.getString(
                            R.string.default_request_error_unknown), productItem.getProductID());
                }
            }

            @Override
            public void onNext(Boolean result) {
                if (isViewAttached()) {
                    if (result) {
                        wishListActionListener.onSuccessAddWishlist(productItem.getProductID());
                    } else
                        wishListActionListener.onErrorRemoveWishlist(context.getString(
                                R.string.default_request_error_unknown), productItem.getProductID());
                }
            }
        };
    }

    private void launchLoginActivity(String productId) {
        Bundle extras = new Bundle();
        extras.putString("product_id", productId);
        getView().launchLoginActivity(extras);
    }

    private void addWishlist(String productId, String userId) {
        addWishlistActionUseCase.createObservable(productId, userId, wishListActionListener);
    }

    private void removeWishlist(String productId, String userId) {
        removeWishlistActionUseCase.createObservable(productId, userId, wishListActionListener);
    }
}
