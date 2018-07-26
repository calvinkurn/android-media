package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.entity.discovery.gql.SearchProductGqlResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterV4UseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetSearchGuideUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.GetDynamicFilterSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.GetQuickFilterSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenterImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;


/**
 * Created by henrypriyono on 10/11/17.
 */

public class ProductListPresenterImpl extends SearchSectionFragmentPresenterImpl<ProductListFragmentView> implements ProductListPresenter {

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
        if (isUsingFilterV4) {
            getDynamicFilterV4UseCase.execute(requestParams, new GetDynamicFilterSubscriber(getView()));
        } else {
            getDynamicFilterUseCase.execute(requestParams, new GetDynamicFilterSubscriber(getView()));
        }
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
        requestDynamicFilter(new HashMap<String, String>());
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
        enrichWithAdditionalParams(requestParams, additionalParams);
        removeDefaultCategoryParam(requestParams);

        Map<String, Object> variables = new HashMap<>();
        variables.put("q", searchParameter.getQueryKey());
        variables.put("start", searchParameter.getStartRow());
        variables.put("rows", 12);
        variables.put("uniqueId", searchParameter.getUniqueID());

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.gql_search_product), SearchProductGqlResponse.class, variables);

        graphqlUseCase.setRequest(graphqlRequest);

        graphqlUseCase.execute(new DefaultSubscriber<GraphqlResponse>() {
                    @Override
                    public void onStart() {
                        getView().incrementStart();
                    }

                    @Override
                    public void onNext(GraphqlResponse objects) {
                        SearchProductGqlResponse gqlResponse = objects.getData(SearchProductGqlResponse.class);
                        ProductViewModel productViewModel
                                = ProductViewModelHelper.convertToProductViewModel(gqlResponse);
                        if (isViewAttached()) {
                            if (productViewModel.getProductList().isEmpty()) {
                                getView().removeLoading();
                                getView().showBottomBarNavigation(false);
                            } else {
                                List<Visitable> list = new ArrayList<Visitable>();
                                getView().removeLoading();
                                list.addAll(productViewModel.getProductList());
                                getView().setProductList(list);
                                if (getView().isEvenPage()) {
                                    getView().addGuidedSearch();
                                }
                                getView().addLoading();
                                getView().showBottomBarNavigation(true);
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
                            getView().showBottomBarNavigation(false);
                        }
                    }
                });
    }

    /*@Override
    public void loadMoreData(final SearchParameter searchParameter, HashMap<String, String> additionalParams) {
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false);
        enrichWithFilterAndSortParams(requestParams);
        enrichWithAdditionalParams(requestParams, additionalParams);
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
                                getView().showBottomBarNavigation(false);
                            } else {
                                List<Visitable> list = new ArrayList<Visitable>();
                                list.addAll(productViewModel.getProductList());
                                getView().setProductList(list);
                                if (getView().isEvenPage()) {
                                    getView().addGuidedSearch();
                                }
                                getView().showBottomBarNavigation(true);
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
                            getView().showNetworkError(searchParameter.getStartRow());
                            getView().showBottomBarNavigation(false);
                        }
                    }
                });
    }*/

    @Override
    public void loadData(final SearchParameter searchParameter, boolean isForceSearch, HashMap<String, String> additionalParams) {
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false, true);
        enrichWithFilterAndSortParams(requestParams);
        enrichWithForceSearchParam(requestParams, isForceSearch);
        enrichWithAdditionalParams(requestParams, additionalParams);
        removeDefaultCategoryParam(requestParams);

        Map<String, Object> variables = new HashMap<>();
        variables.put("q", searchParameter.getQueryKey());
        variables.put("start", 0);
        variables.put("rows", 12);
        variables.put("uniqueId", searchParameter.getUniqueID());

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.gql_search_product_first_page), SearchProductGqlResponse.class, variables);

        graphqlUseCase.setRequest(graphqlRequest);

        graphqlUseCase.execute(new DefaultSubscriber<GraphqlResponse>() {
                    @Override
                    public void onStart() {
                        getView().showRefreshLayout();
                        getView().incrementStart();
                    }

                    @Override
                    public void onCompleted() {
                        //getView().getDynamicFilter();
                        getView().getQuickFilter();
                        getView().hideRefreshLayout();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().removeLoading();
                            getView().showNetworkError(0);
                            getView().hideRefreshLayout();
                            getView().showBottomBarNavigation(false);
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
                                getView().showBottomBarNavigation(false);
                            } else {
                                HeaderViewModel headerViewModel = new HeaderViewModel();
                                headerViewModel.setSuggestionModel(productViewModel.getSuggestionModel());
                                list.add(headerViewModel);
                                list.addAll(productViewModel.getProductList());
                                getView().removeLoading();
                                getView().setProductList(list);
                                getView().addLoading();
                                getView().setTotalSearchResultCount(productViewModel.getSuggestionModel().getFormattedResultCount());
                                getView().showBottomBarNavigation(true);
                            }
                            getView().storeTotalData(productViewModel.getTotalData());
                        }
                    }
                });
    }

    /*@Override
    public void loadData(final SearchParameter searchParameter, boolean isForceSearch, HashMap<String, String> additionalParams) {
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false, true);
        enrichWithFilterAndSortParams(requestParams);
        enrichWithForceSearchParam(requestParams, isForceSearch);
        enrichWithAdditionalParams(requestParams, additionalParams);
        removeDefaultCategoryParam(requestParams);

        getProductUseCase.execute(requestParams,
                new DefaultSubscriber<SearchResultModel>() {
                    @Override
                    public void onStart() {
                        getView().setTopAdsEndlessListener();
                        getView().showRefreshLayout();
                        getView().incrementStart();
                    }

                    @Override
                    public void onCompleted() {
                        getView().getDynamicFilter();
                        getView().getQuickFilter();
                        getView().hideRefreshLayout();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showNetworkError(0);
                            getView().hideRefreshLayout();
                            getView().showBottomBarNavigation(false);
                        }
                    }

                    @Override
                    public void onNext(SearchResultModel searchResultModel) {
                        if (isViewAttached()) {
                            ProductViewModel productViewModel = ProductViewModelHelper.convertToProductViewModelFirstPage(searchResultModel);
                            List<Visitable> list = new ArrayList<Visitable>();
                            if (productViewModel.getProductList().isEmpty()) {
                                getView().setEmptyProduct();
                                getView().setTotalSearchResultCount("0");
                                getView().showBottomBarNavigation(false);
                            } else {
                                HeaderViewModel headerViewModel = new HeaderViewModel();
                                headerViewModel.setSuggestionModel(productViewModel.getSuggestionModel());
                                list.add(headerViewModel);
                                list.addAll(productViewModel.getProductList());
                                getView().setProductList(list);
                                getView().setTotalSearchResultCount(productViewModel.getSuggestionModel().getFormattedResultCount());
                                getView().showBottomBarNavigation(true);
                                if (getView().getStartFrom() > searchResultModel.getTotalData()) {
                                    getView().unSetTopAdsEndlessListener();
                                }
                            }
                            getView().storeTotalData(searchResultModel.getTotalData());
                        }
                    }
                });
    }
    */

    @Override
    public void loadGuidedSearch(String keyword) {
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putString(GetSearchGuideUseCase.PARAM_QUERY, keyword);
        getSearchGuideUseCase.execute(requestParams, new Subscriber<GuidedSearchViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GuidedSearchViewModel guidedSearchViewModel) {
                getView().onGetGuidedSearchComplete(guidedSearchViewModel);
            }
        });
    }

    @Override
    public void requestQuickFilter(HashMap<String, String> additionalParams) {
        RequestParams params = getQuickFilterRequestParams();
        params = enrichWithFilterAndSortParams(params);
        params = enrichWithAdditionalParams(params, additionalParams);
        getDynamicFilterUseCase.execute(params, new GetQuickFilterSubscriber(getView()));
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

    @Override
    public void detachView() {
        super.detachView();
        getProductUseCase.unsubscribe();
        getSearchGuideUseCase.unsubscribe();
        addWishlistActionUseCase.unsubscribe();
        removeWishlistActionUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
        getDynamicFilterV4UseCase.unsubscribe();
    }
}
