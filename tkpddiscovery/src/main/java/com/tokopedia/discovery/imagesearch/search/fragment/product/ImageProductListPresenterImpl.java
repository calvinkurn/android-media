package com.tokopedia.discovery.imagesearch.search.fragment.product;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sachinbansal on 4/13/18.
 */

public class ImageProductListPresenterImpl extends BaseDaggerPresenter<ImageProductListFragmentView> implements ImageProductListPresenter {

    @Inject
    GetProductUseCase getProductUseCase;
    @Inject
    AddWishListUseCase addWishlistActionUseCase;
    @Inject
    RemoveWishListUseCase removeWishlistActionUseCase;
    private WishListActionListener wishListActionListener;
    private Context context;


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
    public void loadMoreData(final SearchParameter searchParameter, HashMap<String, String> additionalParams) {
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false);
        removeDefaultCategoryParam(requestParams);

        getProductUseCase.execute(requestParams,
                new DefaultSubscriber<SearchResultModel>() {
                    @Override
                    public void onStart() {
                        getView().setTopAdsEndlessListener();
                        getView().incrementStart();
                    }

                    @Override
                    public void onNext(SearchResultModel searchResultModel) {
                        ProductViewModel productViewModel
                                = ProductViewModelHelper.convertToProductViewModel(searchResultModel);
                        if (isViewAttached()) {
                            if (productViewModel.getProductList().isEmpty()) {
                                getView().unSetTopAdsEndlessListener();
                            } else {
                                List<Visitable> list = new ArrayList<Visitable>();
                                list.addAll(productViewModel.getProductList());
                                getView().setProductList(list);
                                if (getView().isEvenPage()) {
                                }
                                if (getView().getStartFrom() >= searchResultModel.getTotalData()) {
                                    getView().unSetTopAdsEndlessListener();
                                }
                            }
                            getView().storeTotalData(searchResultModel.getTotalData());
                        }
                    }

                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().hideRefreshLayout();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideRefreshLayout();
                            getView().showNetworkError(searchParameter.getInteger(SearchApiConst.START));
                        }
                    }
                });
    }


    @Override
    public void handleWishlistButtonClicked(ProductItem productItem) {
        if (getView().isUserHasLogin()) {
            getView().disableWishlistButton(productItem.getProductID());
            if (productItem.isWishlisted()) {
                removeWishlist(productItem.getProductID(), getView().getUserId());
            } else {
                addWishlist(productItem.getProductID(), getView().getUserId());
            }
        } else {
            launchLoginActivity(productItem.getProductID());
        }
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

    private void removeDefaultCategoryParam(RequestParams params) {
        if (params.getString(BrowseApi.SC, "").equals(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_SC)) {
            params.clearValue(BrowseApi.SC);
        }
    }

}
