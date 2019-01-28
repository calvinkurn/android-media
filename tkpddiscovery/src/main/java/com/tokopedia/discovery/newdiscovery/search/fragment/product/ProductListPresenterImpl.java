package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.domain.gql.SearchProductGqlResponse;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterV4UseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetSearchGuideUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.ProductWishlistUrlUseCase;
import com.tokopedia.discovery.newdiscovery.helper.GqlSearchHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenterImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.TopAdsViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;


/**
 * Created by henrypriyono on 10/11/17.
 */

public class ProductListPresenterImpl extends SearchSectionFragmentPresenterImpl<ProductListFragmentView> implements ProductListPresenter {

    private static final String TAG = ProductListPresenterImpl.class.getSimpleName();

    @Inject
    GetProductUseCase getProductUseCase;
    @Inject
    GetSearchGuideUseCase getSearchGuideUseCase;
    @Inject
    AddWishListUseCase addWishlistActionUseCase;
    @Inject
    RemoveWishListUseCase removeWishlistActionUseCase;
    @Inject
    GetDynamicFilterUseCase getDynamicFilterUseCase;
    @Inject
    GetDynamicFilterV4UseCase getDynamicFilterV4UseCase;
    @Inject
    ProductWishlistUrlUseCase productWishlistUrlUseCase;
    private WishListActionListener wishlistActionListener;
    GraphqlUseCase graphqlUseCase;
    private Context context;
    private boolean isUsingFilterV4;

    public ProductListPresenterImpl(Context context) {
        this.context = context;
        SearchComponent component = DaggerSearchComponent.builder()
                .appComponent(getComponent(context))
                .build();
        component.inject(this);
        graphqlUseCase = new GraphqlUseCase();
    }

    @Override
    public void attachView(ProductListFragmentView viewListener,
                           WishListActionListener wishlistActionListener) {
        super.attachView(viewListener);
        this.wishlistActionListener = wishlistActionListener;
    }

    @Override
    protected void getFilterFromNetwork(RequestParams requestParams) {
        //TODO will be removed after catalog and shop already migrated also to Gql
    }

    @Override
    public void handleWishlistButtonClicked(final ProductItem productItem) {
        if (getView().isUserHasLogin()) {
            getView().disableWishlistButton(productItem.getProductID());
            if (productItem.isWishlisted()) {
                removeWishlist(productItem.getProductID(), getView().getUserId());
            } else if(productItem.getProductWishlistUrl() != null){
                com.tokopedia.usecase.RequestParams params = com.tokopedia.usecase.RequestParams.create();
                params.putString(ProductWishlistUrlUseCase.PRODUCT_WISHLIST_URL,
                        productItem.getProductWishlistUrl());
                productWishlistUrlUseCase.execute(params, getWishlistSubscriber(productItem));
            } else {
                addWishlist(productItem.getProductID(), getView().getUserId());
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
                    getView().onErrorAddWishList(e.getMessage(), productItem.getProductID());
                    getView().notifyAdapter();
                }
            }

            @Override
            public void onNext(Boolean result) {
                if (isViewAttached()){
                    if (result) {
                        getView().onSuccessAddWishlist(productItem.getProductID());
                    } else {
                        getView().notifyAdapter();
                    }
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
        Log.d(this.toString(), "Add Wishlist " + productId);
        addWishlistActionUseCase.createObservable(productId, userId,
                wishlistActionListener);
    }

    private void removeWishlist(String productId, String userId) {
        Log.d(this.toString(), "Remove Wishlist " + productId);
        removeWishlistActionUseCase.createObservable(productId, userId, wishlistActionListener);
    }

    @Override
    public void requestDynamicFilter() {
        //TODO will be removed after catalog and shop already migrated also to Gql
    }

    @Override
    protected RequestParams getDynamicFilterParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(AuthUtil.generateParamsNetwork2(context, requestParams.getParameters()));
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_PRODUCT);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.Q, getView().getQueryKey());
        if (getView().getSearchParameter().getDepartmentId() != null
                && !getView().getSearchParameter().getDepartmentId().isEmpty()) {
            requestParams.putString(BrowseApi.SC, getView().getSearchParameter().getDepartmentId());
        } else {
            requestParams.putString(BrowseApi.SC, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_SC);
        }
        return requestParams;
    }

    @Override
    public void loadMoreData(final SearchParameter searchParameter, HashMap<String, String> additionalParams) {
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false);
        enrichWithFilterAndSortParams(requestParams);
        enrichWithRelatedSearchParam(requestParams, true);
        enrichWithAdditionalParams(requestParams, additionalParams);
        removeDefaultCategoryParam(requestParams);

        Subscriber<GraphqlResponse> subscriber = new DefaultSubscriber<GraphqlResponse>() {
            @Override
            public void onStart() {
                if (isViewAttached()) {
                    getView().incrementStart();
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                SearchProductGqlResponse gqlResponse = objects.getData(SearchProductGqlResponse.class);
                ProductViewModel productViewModel
                        = ProductViewModelHelper.convertToProductViewModel(gqlResponse);
                if (isViewAttached()) {
                    if (productViewModel.getProductList().isEmpty()) {
                        getView().removeLoading();
                    } else {
                        List<Visitable> list = new ArrayList<Visitable>();
                        getView().removeLoading();
                        if(!productViewModel.getAdsModel().getData().isEmpty()) {
                            list.add(new TopAdsViewModel(productViewModel.getAdsModel(), productViewModel.getQuery()));
                        }
                        list.addAll(productViewModel.getProductList());
                        getView().setProductList(list);
                        getView().addLoading();
                    }
                    getView().storeTotalData(productViewModel.getTotalData());
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
                    getView().removeLoading();
                    getView().hideRefreshLayout();
                    getView().showNetworkError(searchParameter.getStartRow());
                }
            }
        };
        GqlSearchHelper.requestProductLoadMore(context, requestParams, graphqlUseCase, subscriber);
    }

    @Override
    public void loadData(final SearchParameter searchParameter, boolean isForceSearch, HashMap<String, String> additionalParams) {
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false, true);
        enrichWithFilterAndSortParams(requestParams);
        enrichWithForceSearchParam(requestParams, isForceSearch);
        enrichWithRelatedSearchParam(requestParams, true);
        enrichWithAdditionalParams(requestParams, additionalParams);
        removeDefaultCategoryParam(requestParams);

        Subscriber<GraphqlResponse> subscriber = new DefaultSubscriber<GraphqlResponse>() {
            @Override
            public void onStart() {
                if (isViewAttached()) {
                    getView().showRefreshLayout();
                    getView().incrementStart();
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
                    getView().removeLoading();
                    getView().showNetworkError(0);
                    getView().hideRefreshLayout();
                }
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                if (isViewAttached()) {
                    SearchProductGqlResponse gqlResponse = objects.getData(SearchProductGqlResponse.class);
                    ProductViewModel productViewModel
                            = ProductViewModelHelper.convertToProductViewModelFirstPageGql(gqlResponse);
                    List<Visitable> list = new ArrayList<Visitable>();
                    if (productViewModel.getProductList().isEmpty()) {
                        getView().removeLoading();
                        getView().setEmptyProduct();
                        getView().setTotalSearchResultCount("0");
                    } else {
                        HeaderViewModel headerViewModel = new HeaderViewModel();
                        headerViewModel.setSuggestionModel(productViewModel.getSuggestionModel());
                        if (productViewModel.getGuidedSearchViewModel() != null) {
                            headerViewModel.setGuidedSearch(productViewModel.getGuidedSearchViewModel());
                        }
                        if (productViewModel.getQuickFilterModel() != null
                                && productViewModel.getQuickFilterModel().getFilter() != null) {
                            headerViewModel.setQuickFilterList(getView().getQuickFilterOptions(productViewModel.getQuickFilterModel()));
                        }
                        list.add(headerViewModel);
                        if(!gqlResponse.getTopAdsModel().getData().isEmpty()) {
                            list.add(new TopAdsViewModel(gqlResponse.getTopAdsModel(), productViewModel.getQuery()));
                        }
                        list.addAll(productViewModel.getProductList());
                        if (productViewModel.getRelatedSearchModel() != null) {
                            list.add(productViewModel.getRelatedSearchModel());
                        }
                        getView().removeLoading();
                        getView().setProductList(list);
                        getView().addLoading();
                        getView().setTotalSearchResultCount(productViewModel.getSuggestionModel().getFormattedResultCount());
                        getView().stopTracePerformanceMonitoring();
                    }
                    getView().storeTotalData(productViewModel.getTotalData());
                    getView().renderDynamicFilter(productViewModel.getDynamicFilterModel());
                }
            }
        };
        GqlSearchHelper.requestProductFirstPage(context, requestParams, graphqlUseCase, subscriber);
    }

    @Override
    public void setIsUsingFilterV4(boolean isUsingFilterV4) {
        this.isUsingFilterV4 = isUsingFilterV4;
    }

    private RequestParams getQuickFilterRequestParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(AuthUtil.generateParamsNetwork2(context, requestParams.getParameters()));
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_QUICK_FILTER);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.Q, getView().getQueryKey());
        if (getView().getSearchParameter().getDepartmentId() != null
                && !getView().getSearchParameter().getDepartmentId().isEmpty()) {
            requestParams.putString(BrowseApi.SC, getView().getSearchParameter().getDepartmentId());
        } else {
            requestParams.putString(BrowseApi.SC, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_SC);
        }
        return requestParams;
    }

    private void enrichWithForceSearchParam(RequestParams requestParams, boolean isForceSearch) {
        requestParams.putBoolean(BrowseApi.REFINED, isForceSearch);
    }

    private void enrichWithRelatedSearchParam(RequestParams requestParams, boolean relatedSearchEnabled) {
        requestParams.putBoolean(BrowseApi.RELATED, relatedSearchEnabled);
    }

    @Override
    public void detachView() {
        super.detachView();
        getProductUseCase.unsubscribe();
        getSearchGuideUseCase.unsubscribe();
        addWishlistActionUseCase.unsubscribe();
        removeWishlistActionUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
        getDynamicFilterV4UseCase.unsubscribe();
        graphqlUseCase.unsubscribe();
    }
}
