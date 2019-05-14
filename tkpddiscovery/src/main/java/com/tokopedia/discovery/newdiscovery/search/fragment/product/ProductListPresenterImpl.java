package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.domain.gql.SearchFilterProductGqlResponse;
import com.tokopedia.discovery.newdiscovery.domain.gql.SearchProductGqlResponse;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
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
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSessionInterface;
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
    AddWishListUseCase addWishlistActionUseCase;
    @Inject
    RemoveWishListUseCase removeWishlistActionUseCase;
    @Inject
    ProductWishlistUrlUseCase productWishlistUrlUseCase;
    @Inject
    UserSessionInterface userSession;

    private WishListActionListener wishlistActionListener;
    GraphqlUseCase graphqlUseCase;
    private Context context;
    private boolean isUsingFilterV4;
    private boolean enableGlobalNavWidget;

    private Subscriber<GraphqlResponse> loadDataSubscriber;
    private Subscriber<GraphqlResponse> loadMoreDataSubscriber;

    public ProductListPresenterImpl(Context context) {
        this.context = context;
        SearchComponent component = DaggerSearchComponent.builder()
                .appComponent(getComponent(context))
                .build();
        component.inject(this);
        graphqlUseCase = new GraphqlUseCase();
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        enableGlobalNavWidget = remoteConfig.getBoolean(
                RemoteConfigKey.ENABLE_GLOBAL_NAV_WIDGET,false);
    }

    @Override
    public void attachView(ProductListFragmentView viewListener,
                           WishListActionListener wishlistActionListener) {
        super.attachView(viewListener);
        this.wishlistActionListener = wishlistActionListener;
    }

    @Override
    public void requestDynamicFilter() {
        if (getView() == null) {
            return;
        }
        RequestParams params = RequestParams.create();
        params.putAll(getView().getSearchParameter().getSearchParameterHashMap());
        enrichWithAdditionalParams(params, getView().getAdditionalParamsMap());
        Subscriber<GraphqlResponse> subscriber = getFilterFromNetworkSubscriber();
        GqlSearchHelper.requestDynamicFilter(context, params, graphqlUseCase, subscriber);
    }

    @Override
    protected void getFilterFromNetwork(RequestParams requestParams) {

    }

    private Subscriber<GraphqlResponse> getFilterFromNetworkSubscriber() {
        return new Subscriber<GraphqlResponse>() {
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

            @Override
            public void onCompleted() {

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
        return RequestParams.EMPTY;
    }

    private HashMap<String, String> generateParamsNetwork(RequestParams requestParams) {
        return new HashMap<>(
                AuthUtil.generateParamsNetwork(userSession.getUserId(),
                        userSession.getDeviceId(),
                        requestParams.getParamsAllValueInString()));
    }

    @Override
    public void loadMoreData(final SearchParameter searchParameter, HashMap<String, String> additionalParams) {
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter);
        enrichWithRelatedSearchParam(requestParams, true);
        enrichWithAdditionalParams(requestParams, additionalParams);

        unsubscribeLoadMoreDataSubscriberIfStillSubscribe();

        loadMoreDataSubscriber = getLoadMoreDataSubscriber(searchParameter);

        GqlSearchHelper.requestProductLoadMore(context, requestParams, graphqlUseCase, loadMoreDataSubscriber);
    }

    private void unsubscribeLoadMoreDataSubscriberIfStillSubscribe() {
        if(loadMoreDataSubscriber != null && !loadMoreDataSubscriber.isUnsubscribed()) {
            loadMoreDataSubscriber.unsubscribe();
        }
    }

    private Subscriber<GraphqlResponse> getLoadMoreDataSubscriber(final SearchParameter searchParameter) {
        return new Subscriber<GraphqlResponse>() {
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
    public void loadData(final SearchParameter searchParameter, HashMap<String, String> additionalParams, boolean isFirstTimeLoad) {
        RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, true);
        enrichWithRelatedSearchParam(requestParams, true);
        if (isViewAttached() && getView().isAnyFilterActive()) {
            enrichWithAdditionalParams(requestParams, additionalParams);
        }

        unsubscribeLoadDataSubscriberIfStillSubscribe();

        loadDataSubscriber = getLoadDataSubscriber(isFirstTimeLoad);

        GqlSearchHelper.requestProductFirstPage(context, requestParams, graphqlUseCase, loadDataSubscriber);
    }

    private void unsubscribeLoadDataSubscriberIfStillSubscribe() {
        if(loadDataSubscriber != null && !loadDataSubscriber.isUnsubscribed()) {
            loadDataSubscriber.unsubscribe();
        }
    }

    private Subscriber<GraphqlResponse> getLoadDataSubscriber(final boolean isFirstTimeLoad) {
        return new Subscriber<GraphqlResponse>() {
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
            getView().getDynamicFilter();
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
        boolean isGlobalNavWidgetAvailable
                = productViewModel.getGlobalNavViewModel() != null && enableGlobalNavWidget;
        if (isGlobalNavWidgetAvailable) {
            headerViewModel.setGlobalNavViewModel(productViewModel.getGlobalNavViewModel());
            getView().sendImpressionGlobalNav(productViewModel.getGlobalNavViewModel());
        }
        if (productViewModel.getCpmModel() != null && !isGlobalNavWidgetAvailable) {
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

    private void enrichWithRelatedSearchParam(RequestParams requestParams, boolean relatedSearchEnabled) {
        requestParams.putBoolean(SearchApiConst.RELATED, relatedSearchEnabled);
    }

    @Override
    public void detachView() {
        super.detachView();
        addWishlistActionUseCase.unsubscribe();
        removeWishlistActionUseCase.unsubscribe();
        graphqlUseCase.unsubscribe();
    }
}