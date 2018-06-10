package com.tokopedia.discovery.newdiscovery.category.presentation.product;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.newdiscovery.category.di.component.CategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.di.component.DaggerCategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.helper.CategoryModelHelper;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.subscriber.AddWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.subscriber.RemoveWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.domain.usecase.AddWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.RemoveWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.GetDynamicFilterSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenterImpl;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by alifa on 10/26/17.
 */

public class ProductPresenter extends SearchSectionFragmentPresenterImpl<ProductContract.View> implements ProductContract.Presenter {

    @Inject
    GetProductUseCase getProductUseCase;
    @Inject
    AddWishlistActionUseCase addWishlistActionUseCase;
    @Inject
    RemoveWishlistActionUseCase removeWishlistActionUseCase;
    @Inject
    GetDynamicFilterUseCase getDynamicFilterUseCase;
    private WishlistActionListener wishlistActionListener;
    private ProductContract.View viewListener;
    private Context context;

    public ProductPresenter(Context context) {
        this.context = context;
        CategoryComponent component = DaggerCategoryComponent.builder()
                .appComponent(getComponent(context))
                .build();
        component.inject(this);
    }

    @Override
    public void attachView(ProductContract.View viewListener,
                           WishlistActionListener wishlistActionListener) {
        super.attachView(viewListener);
        this.viewListener = viewListener;
        this.wishlistActionListener = wishlistActionListener;
    }

    @Override
    public void handleWishlistButtonClicked(ProductItem productItem, int adapterPosition) {
        if (viewListener.isUserHasLogin()) {
            viewListener.disableWishlistButton(adapterPosition);
            if (productItem.isWishlisted()) {
                removeWishlist(productItem.getProductID(), viewListener.getUserId(), adapterPosition);
            } else {
                addWishlist(productItem.getProductID(), viewListener.getUserId(), adapterPosition);
            }
        } else {
            launchLoginActivity(productItem.getProductID());
        }
    }

    private void launchLoginActivity(String productId) {
        Bundle extras = new Bundle();
        extras.putString("product_id", productId);
        viewListener.launchLoginActivity(extras);
    }

    private void addWishlist(String productId, String userId, int adapterPosition) {
        addWishlistActionUseCase.execute(AddWishlistActionUseCase.generateParam(productId, userId),
                new AddWishlistActionSubscriber(wishlistActionListener, adapterPosition));
    }

    private void removeWishlist(String productId, String userId, int adapterPosition) {
        removeWishlistActionUseCase.execute(RemoveWishlistActionUseCase.generateParam(productId, userId),
                new RemoveWishlistActionSubscriber(wishlistActionListener, adapterPosition));
    }

    @Override
    protected void getFilterFromNetwork(RequestParams requestParams) {
        getDynamicFilterUseCase.execute(requestParams, new GetDynamicFilterSubscriber(getView()));
    }

    @Override
    protected RequestParams getDynamicFilterParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(AuthUtil.generateParamsNetwork2(context, requestParams.getParameters()));
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);

        if (isViewAttached()) {
            requestParams.putString(BrowseApi.SC, getView().getDepartmentId());

            if (getView().getSelectedSort() != null) {
                requestParams.putAll(getView().getSelectedSort());
            }
            if (getView().getSelectedFilter() != null) {
                requestParams.putAll(getView().getSelectedFilter());
            }
        }
        return requestParams;
    }

    @Override
    public void loadDataProduct(final SearchParameter searchParameter, final CategoryHeaderModel categoryHeaderModel) {
        RequestParams requestParams
                = enrichWithFilterAndSortParams(GetProductUseCase.createInitializeSearchParam(searchParameter));
        removeDefaultCategoryParam(requestParams);
        getProductUseCase.execute(requestParams, new DefaultSubscriber<SearchResultModel>(){
            @Override
            public void onStart() {
                if(isViewAttached()){
                    getView().showRefreshLayout();
                    getView().setTopAdsEndlessListener();
                }
            }

            @Override
            public void onNext(SearchResultModel searchResultModel) {
                if(isViewAttached()){
                    int page = (searchParameter.getStartRow() / 12) + 1;
                    getView().clearLastProductTracker(page == 1);
                    ProductViewModel productViewModel
                            = CategoryModelHelper.convertToProductViewModel(searchResultModel, categoryHeaderModel);
                    if(productViewModel.getProductList().isEmpty()){
                        getView().unSetTopAdsEndlessListener();
                        getView().showEmptyProduct();
                    } else {
                        productViewModel.setProductList(getView().mappingTrackerProduct(productViewModel.getProductList(), page));
                        getView().trackEnhanceProduct(getView().createImpressionProductDataLayer(productViewModel.getProductList()));
                        List<Visitable> list = new ArrayList<Visitable>();
                        list.add(productViewModel.getCategoryHeaderModel());
                        list.addAll(productViewModel.getProductList());
                        getView().setProductList(list);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().showNetworkError(0);
                    getView().hideRefreshLayout();
                }
            }

            @Override
            public void onCompleted() {
                if(isViewAttached()){
                    getView().hideRefreshLayout();
                    viewListener.getDynamicFilter();
                }
            }
        });
    }

    @Override
    public void loadMore(SearchParameter searchParameter, ProductPresenter.LoadMoreListener loadMoreListener) {
        RequestParams requestParams
                = enrichWithFilterAndSortParams(GetProductUseCase.createInitializeSearchParam(searchParameter,false));
        removeDefaultCategoryParam(requestParams);
        getProductUseCase.execute(requestParams, createLoadMoreSubscriber(loadMoreListener));
    }

    private Subscriber<SearchResultModel> createLoadMoreSubscriber(final ProductPresenter.LoadMoreListener loadMoreListener) {
        return new Subscriber<SearchResultModel>() {
            @Override
            public void onCompleted() {
                viewListener.getDynamicFilter();
            }

            @Override
            public void onError(Throwable e) {
                loadMoreListener.onFailed();
            }

            @Override
            public void onNext(SearchResultModel searchResultModel) {
                ProductViewModel productViewModel
                        = CategoryModelHelper.convertToProductViewModel(searchResultModel);
                loadMoreListener.onSuccess(productViewModel.getProductList());
            }
        };
    }

    interface LoadMoreListener {
        void onSuccess(List<ProductItem> productItemList);
        void onFailed();
    }

    @Override
    public void detachView() {
        super.detachView();
        getProductUseCase.unsubscribe();
        addWishlistActionUseCase.unsubscribe();
        removeWishlistActionUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
    }
}
