package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.domain.usecase.AddWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.RemoveWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.GetDynamicFilterSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenterImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ProductViewModelHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.subscriber.AddWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.subscriber.RemoveWishlistActionSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by henrypriyono on 10/11/17.
 */

public class ProductListPresenterImpl extends SearchSectionFragmentPresenterImpl<ProductListFragmentView> implements ProductListPresenter {

    @Inject
    GetProductUseCase getProductUseCase;
    @Inject
    AddWishlistActionUseCase addWishlistActionUseCase;
    @Inject
    RemoveWishlistActionUseCase removeWishlistActionUseCase;
    @Inject
    GetDynamicFilterUseCase getDynamicFilterUseCase;
    private WishlistActionListener wishlistActionListener;
    private Context context;

    public ProductListPresenterImpl(Context context) {
        this.context = context;
        SearchComponent component = DaggerSearchComponent.builder()
                .appComponent(getComponent(context))
                .build();
        component.inject(this);
    }

    @Override
    public void attachView(ProductListFragmentView viewListener,
                           WishlistActionListener wishlistActionListener) {
        super.attachView(viewListener);
        this.wishlistActionListener = wishlistActionListener;
    }

    @Override
    protected void getFilterFromNetwork(RequestParams requestParams) {
        getDynamicFilterUseCase.execute(requestParams, new GetDynamicFilterSubscriber(getView()));
    }

    @Override
    public void handleWishlistButtonClicked(ProductItem productItem, int adapterPosition) {
        if (getView().isUserHasLogin()) {
            getView().disableWishlistButton(adapterPosition);
            if (productItem.isWishlisted()) {
                removeWishlist(productItem.getProductID(), getView().getUserId(), adapterPosition);
            } else {
                addWishlist(productItem.getProductID(), getView().getUserId(), adapterPosition);
            }
        } else {
            launchLoginActivity(productItem.getProductID());
        }
    }

    private void launchLoginActivity(String productId) {
        Bundle extras = new Bundle();
        extras.putInt(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        extras.putString("product_id", productId);
        getView().launchLoginActivity(extras);
    }

    private void addWishlist(String productId, String userId, int adapterPosition) {
        Log.d(this.toString(), "Add Wishlist " + productId);
        addWishlistActionUseCase.execute(AddWishlistActionUseCase.generateParam(productId, userId),
                new AddWishlistActionSubscriber(wishlistActionListener, adapterPosition));
    }

    private void removeWishlist(String productId, String userId, int adapterPosition) {
        Log.d(this.toString(), "Remove Wishlist " + productId);
        removeWishlistActionUseCase.execute(RemoveWishlistActionUseCase.generateParam(productId, userId),
                new RemoveWishlistActionSubscriber(wishlistActionListener, adapterPosition));
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
    public void loadMoreData(SearchParameter searchParameter, HashMap<String, String> additionalParams) {
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
                            getView().showNetworkError(0);
                            getView().showBottomBarNavigation(false);
                        }
                    }
                });
    }

    @Override
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
                        getView().incrementStart();
                    }

                    @Override
                    public void onCompleted() {
                        getView().getDynamicFilter();
                        getView().hideRefreshLayout();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showNetworkError(searchParameter.getStartRow());
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
                                getView().showBottomBarNavigation(false);
                            } else {
                                HeaderViewModel headerViewModel = new HeaderViewModel();
                                headerViewModel.setSuggestionModel(productViewModel.getSuggestionModel());
                                list.add(headerViewModel);
                                list.addAll(productViewModel.getProductList());
                                getView().setProductList(list);
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

    private void enrichWithForceSearchParam(RequestParams requestParams, boolean isForceSearch) {
        requestParams.putBoolean(BrowseApi.REFINED, isForceSearch);
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
