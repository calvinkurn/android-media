package com.tokopedia.discovery.similarsearch.view.presenter;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.domain.usecase.AddWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.RemoveWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.subscriber.AddWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.subscriber.RemoveWishlistActionSubscriber;
import com.tokopedia.discovery.similarsearch.analytics.SimilarSearchTracking;
import com.tokopedia.discovery.similarsearch.domain.GetSimilarProductUseCase;
import com.tokopedia.discovery.similarsearch.model.ProductsItem;
import com.tokopedia.discovery.similarsearch.model.SearchProductList;
import com.tokopedia.discovery.similarsearch.view.SimilarSearchContract;
import com.tokopedia.discovery.similarsearch.view.SimilarSearchFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class SimilarSearchPresenter extends BaseDaggerPresenter<SimilarSearchContract.View> implements SimilarSearchContract.Presenter{


    AddWishlistActionUseCase addWishlistActionUseCase;
    RemoveWishlistActionUseCase removeWishlistActionUseCase;
    GetSimilarProductUseCase getSimilarProductUseCase;

    private WishlistActionListener wishListListener;


    @Inject
    public SimilarSearchPresenter(AddWishlistActionUseCase addWishlistActionUseCase, RemoveWishlistActionUseCase removeWishlistActionUseCase, GetSimilarProductUseCase getSimilarProductUseCase) {
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
                getView().setEmptyLayoutVisible();
                getView().setContentLayoutGone();
            }

            @Override
            public void onNext(List<ProductsItem> productsItems) {
                if(productsItems != null && productsItems.size() > 0) {
                    SimilarSearchTracking.eventUserSeeSimilarProduct(getView().getProductID());
                    getView().setProductList(productsItems);
                }else {
                    getView().setEmptyLayoutVisible();
                    SimilarSearchTracking.eventUserSeeNoSimilarProduct(getView().getProductID());
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
        SimilarSearchTracking.eventAddWishList(productId);
        addWishlistActionUseCase.execute(AddWishlistActionUseCase.generateParam(productId, userId),
                new AddWishlistActionSubscriber(wishListListener, adapterPosition));
    }


    private void removeWishlist(String productId, String userId, int adapterPosition) {
        SimilarSearchTracking.eventRemoveWishList(productId);
        removeWishlistActionUseCase.execute(RemoveWishlistActionUseCase.generateParam(productId, userId),
                new RemoveWishlistActionSubscriber(wishListListener, adapterPosition));
    }

    public void setWishListListener(SimilarSearchFragment wishListListener) {
        this.wishListListener = wishListListener;
    }
}
