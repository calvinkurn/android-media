package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.domain.gql.SearchFilterProductGqlResponse;
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
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

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

    private Subscriber<GraphqlResponse> loadDataSubscriber;
    private Subscriber<GraphqlResponse> loadMoreDataSubscriber;

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
        Subscriber<GraphqlResponse> subscriber = getFilterFromNetworkSubscriber();
        GqlSearchHelper.requestDynamicFilter(context, requestParams, graphqlUseCase, subscriber);
    }

    private Subscriber<GraphqlResponse> getFilterFromNetworkSubscriber() {
        return new DefaultSubscriber<GraphqlResponse>() {
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().renderFailGetDynamicFilter();
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                SearchFilterProductGqlResponse searchFilterProductGqlResponse =
                        graphqlResponse.getData(SearchFilterProductGqlResponse.class);

                if (searchFilterProductGqlResponse != null) {
                    DynamicFilterModel dynamicFilterModel = searchFilterProductGqlResponse.getDynamicFilterModel();

                    getView().renderDynamicFilter(dynamicFilterModel);
                }
            }
        };
    }

    @Override
    public void handleWishlistButtonClicked(final ProductItem productItem) {
        if (getView().isUserHasLogin()) {
            getView().disableWishlistButton(productItem.getProductID());
            if (productItem.isWishlisted()) {
                removeWishlist(productItem.getProductID(), getView().getUserId());
            } else if(productItem.isTopAds()){
                com.tokopedia.usecase.RequestParams params = com.tokopedia.usecase.RequestParams.create();
                params.putString(ProductWishlistUrlUseCase.PRODUCT_WISHLIST_URL,
                        productItem.getTopadsWishlistUrl());
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
    protected RequestParams getDynamicFilterParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(AuthUtil.generateParamsNetwork2(context, requestParams.getParameters()));
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_PRODUCT);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.Q, getView().getQueryKey());
        if(!TextUtils.isEmpty(getView().getSearchParameter().get(SearchApiConst.SC))) {
            requestParams.putString(BrowseApi.SC, getView().getSearchParameter().get(SearchApiConst.SC));
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

        unsubscribeLoadMoreDataSubscriberIfStillSubscribe();

        loadMoreDataSubscriber = getLoadMoreDataSubscriber(searchParameter);

        GqlSearchHelper.requestProductLoadMore(context, requestParams, graphqlUseCase, loadMoreDataSubscriber);
    }

    private void unsubscribeLoadMoreDataSubscriberIfStillSubscribe() {
        if(loadMoreDataSubscriber != null && !loadMoreDataSubscriber.isUnsubscribed()) {
            loadMoreDataSubscriber.unsubscribe();
        }
    }

    private DefaultSubscriber<GraphqlResponse> getLoadMoreDataSubscriber(final SearchParameter searchParameter) {
        return new DefaultSubscriber<GraphqlResponse>() {
            @Override
            public void onStart() {
                loadMoreDataSubscriberOnStartIfViewAttached();
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                loadMoreDataSubscriberOnNextIfViewAttached(objects);
            }

            @Override
            public void onCompleted() {
                loadMoreDataSubscriberOnCompleteIfViewAttached();
            }

            @Override
            public void onError(Throwable e) {
                loadMoreDataSubscriberOnErrorIfViewAttached(searchParameter);
            }
        };
    }

    private void loadMoreDataSubscriberOnStartIfViewAttached() {
        if (isViewAttached()) {
            getView().incrementStart();
        }
    }

    private void loadMoreDataSubscriberOnNextIfViewAttached(GraphqlResponse objects) {
        SearchProductGqlResponse gqlResponse = objects.getData(SearchProductGqlResponse.class);

        if(gqlResponse == null) return;

        ProductViewModel productViewModel
                = ProductViewModelHelper.convertToProductViewModel(gqlResponse);

        if (isViewAttached()) {
            if (productViewModel.getProductList().isEmpty()) {
                getViewToRemoveLoading();
            } else {
                getViewToShowMoreData(productViewModel);
            }

            getView().storeTotalData(productViewModel.getTotalData());
        }
    }

    private void getViewToRemoveLoading() {
        getView().removeLoading();
    }

    private void getViewToShowMoreData(ProductViewModel productViewModel) {
        List<Visitable> list = new ArrayList<>(ProductViewModelHelper.convertToListOfVisitable(productViewModel));
        getView().removeLoading();
        getView().addProductList(list);
        getView().addLoading();
    }

    private void loadMoreDataSubscriberOnCompleteIfViewAttached() {
        if (isViewAttached()) {
            getView().hideRefreshLayout();
        }
    }

    private void loadMoreDataSubscriberOnErrorIfViewAttached(SearchParameter searchParameter) {
        if (isViewAttached()) {
            getView().removeLoading();
            getView().hideRefreshLayout();
            getView().showNetworkError(searchParameter.getInteger(SearchApiConst.START));
        }
    }

    @Override
    public void loadData(final SearchParameter searchParameter, boolean isForceSearch, HashMap<String, String> additionalParams, boolean isFirstTimeLoad) {
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false, true);
        enrichWithFilterAndSortParams(requestParams);
        enrichWithForceSearchParam(requestParams, isForceSearch);
        enrichWithRelatedSearchParam(requestParams, true);
        enrichWithAdditionalParams(requestParams, additionalParams);
        removeDefaultCategoryParam(requestParams);

        unsubscribeLoadDataSubscriberIfStillSubscribe();

        loadDataSubscriber = getLoadDataSubscriber(isFirstTimeLoad);

        GqlSearchHelper.requestProductFirstPage(context, requestParams, graphqlUseCase, loadDataSubscriber);
    }

    private void unsubscribeLoadDataSubscriberIfStillSubscribe() {
        if(loadDataSubscriber != null && !loadDataSubscriber.isUnsubscribed()) {
            loadDataSubscriber.unsubscribe();
        }
    }

    private DefaultSubscriber<GraphqlResponse> getLoadDataSubscriber(final boolean isFirstTimeLoad) {
        return new DefaultSubscriber<GraphqlResponse>() {
            @Override
            public void onStart() {
                loadDataSubscriberOnStartIfViewAttached();
            }

            @Override
            public void onCompleted() {
                loadDataSubscriberOnCompleteIfViewAttached();
            }

            @Override
            public void onError(Throwable e) {
                loadDataSubscriberOnErrorIfViewAttached();
            }

            @Override
            public void onNext(GraphqlResponse objects) {
                loadDataSubscriberOnNextIfViewAttached(objects, isFirstTimeLoad);
            }
        };
    }

    private void loadDataSubscriberOnStartIfViewAttached() {
        if (isViewAttached()) {
            getView().showRefreshLayout();
            getView().incrementStart();
        }
    }

    private void loadDataSubscriberOnCompleteIfViewAttached() {
        if (isViewAttached()) {
            getView().hideRefreshLayout();
        }
    }

    private void loadDataSubscriberOnErrorIfViewAttached() {
        if (isViewAttached()) {
            getView().removeLoading();
            getView().showNetworkError(0);
            getView().hideRefreshLayout();
        }
    }

    private void loadDataSubscriberOnNextIfViewAttached(GraphqlResponse objects, boolean isFirstTimeLoad) {
        if (isViewAttached()) {
            SearchProductGqlResponse gqlResponse = objects.getData(SearchProductGqlResponse.class);
            ProductViewModel productViewModel
                    = ProductViewModelHelper.convertToProductViewModelFirstPageGql(gqlResponse);

            if (productViewModel.getProductList().isEmpty()) {
                getViewToShowEmptySearch();
            } else {
                getViewToShowProductList(productViewModel);
            }

            getView().storeTotalData(productViewModel.getTotalData());
            getView().renderDynamicFilter(productViewModel.getDynamicFilterModel());

            if(isFirstTimeLoad) {
                getViewToSendTrackingOnFirstTimeLoad(productViewModel);
            }
        }
    }

    private void getViewToShowEmptySearch() {
        getView().removeLoading();
        getView().setEmptyProduct();
        getView().setTotalSearchResultCount("0");
    }

    private void getViewToShowProductList(ProductViewModel productViewModel) {
        List<Visitable> list = new ArrayList<>();

        HeaderViewModel headerViewModel = new HeaderViewModel();
        headerViewModel.setSuggestionModel(productViewModel.getSuggestionModel());
        if (productViewModel.getGuidedSearchViewModel() != null) {
            headerViewModel.setGuidedSearch(productViewModel.getGuidedSearchViewModel());
        }
        if (productViewModel.getQuickFilterModel() != null
                && productViewModel.getQuickFilterModel().getFilter() != null) {
            headerViewModel.setQuickFilterList(getView().getQuickFilterOptions(productViewModel.getQuickFilterModel()));
        }
        if (productViewModel.getCpmModel() != null) {
            headerViewModel.setCpmModel(productViewModel.getCpmModel());
        }
        list.add(headerViewModel);
        list.addAll(ProductViewModelHelper.convertToListOfVisitable(productViewModel));
        if (productViewModel.getRelatedSearchModel() != null) {
            list.add(productViewModel.getRelatedSearchModel());
        }

        getView().setAdditionalParams(productViewModel.getAdditionalParams());
        getView().removeLoading();
        getView().setProductList(list);
        getView().initQuickFilter(productViewModel.getQuickFilterModel().getFilter());
        getView().addLoading();
        getView().setTotalSearchResultCount(productViewModel.getSuggestionModel().getFormattedResultCount());
        getView().stopTracePerformanceMonitoring();
    }

    private void getViewToSendTrackingOnFirstTimeLoad(ProductViewModel productViewModel) {
        JSONArray afProdIds = new JSONArray();
        HashMap<String, String> category = new HashMap<String, String>();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (productViewModel.getProductList().size() > 0) {
            for (int i = 0; i < productViewModel.getProductList().size(); i++) {
                if (i < 3) {
                    prodIdArray.add(productViewModel.getProductList().get(i).getProductID());
                    afProdIds.put(productViewModel.getProductList().get(i).getProductID());
                } else {
                    break;
                }
                category.put(String.valueOf(productViewModel.getProductList().get(i).getCategoryID()), productViewModel.getProductList().get(i).getCategoryName());

            }
        }

        getView().sendTrackingEventAppsFlyerViewListingSearch(afProdIds, productViewModel.getQuery(), prodIdArray);
        getView().sendTrackingEventMoEngageSearchAttempt(productViewModel.getQuery(), !productViewModel.getProductList().isEmpty(), category);
        getView().setFirstTimeLoad(false);
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
        if (!TextUtils.isEmpty(getView().getSearchParameter().get(SearchApiConst.SC))) {
            requestParams.putString(BrowseApi.SC, getView().getSearchParameter().get(SearchApiConst.SC));
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