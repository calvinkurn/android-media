package com.tokopedia.discovery.imagesearch.search.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.applink.ApplinkConstInternal;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListAdapter;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListFragmentView;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListPresenter;
import com.tokopedia.discovery.imagesearch.search.fragment.product.adapter.typefactory.ImageProductListTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.ProductItemDecoration;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.NetworkParamHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.discovery.similarsearch.SimilarSearchManager;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.core.home.helper.ProductFeedHelper.calcColumnSize;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchProductListFragment extends BaseDaggerFragment implements
        SearchSectionGeneralAdapter.OnItemChangeView, ImageProductListFragmentView,
        ProductListener, WishListActionListener, TopAdsItemClickListener, TopAdsListener {

    public static final int REQUEST_CODE_LOGIN = 561;
    private static final int REQUEST_CODE_GOTO_PRODUCT_DETAIL = 123;

    private static final String EXTRA_PRODUCT_LIST = "EXTRA_PRODUCT_LIST";
    private static final String EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER";
    private static final String EXTRA_SPAN_COUNT = "EXTRA_SPAN_COUNT";

    protected RecyclerView recyclerView;
    @Inject
    ImageProductListPresenter presenter;
    @Inject
    UserSessionInterface userSession;

    private GCMHandler gcmHandler;
    private Config topAdsConfig;
    private ImageProductListAdapter adapter;
    protected TopAdsRecyclerAdapter topAdsRecyclerAdapter;
    private ProductViewModel productViewModel;
    private ProductListTypeFactory imageProductListTypeFactory;
    private SearchParameter searchParameter;
    private GridLayoutManager gridLayoutManager;

    private RedirectionListener redirectionListener;
    private static final int MAXIMUM_PRODUCT_COUNT_FOR_ONE_EVENT = 12;

    public int spanCount;

    private static final String ARG_VIEW_MODEL = "ARG_VIEW_MODEL";



    public static ImageSearchProductListFragment newInstance(ProductViewModel productViewModel) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_VIEW_MODEL, productViewModel);
        ImageSearchProductListFragment imageSearchProductListFragment = new ImageSearchProductListFragment();
        imageSearchProductListFragment.setArguments(args);
        return imageSearchProductListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            loadDataFromSavedState(savedInstanceState);
        } else {
            loadDataFromArguments();
        }
        gcmHandler = new GCMHandler(getContext());
    }

    private void loadDataFromSavedState(Bundle savedInstanceState) {
        productViewModel = savedInstanceState.getParcelable(EXTRA_PRODUCT_LIST);
        setSearchParameter((SearchParameter) savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER));
    }

    private void loadDataFromArguments() {
        productViewModel = getArguments().getParcelable(ARG_VIEW_MODEL);
        if (productViewModel != null) {

            if (productViewModel.getSearchParameter() != null)
                setSearchParameter(productViewModel.getSearchParameter());
        }
    }


    @Override
    protected String getScreenName() {
        return getScreenNameId();
    }

    @Override
    protected void initInjector() {
        SearchComponent component = DaggerSearchComponent.builder()
                .appComponent(getComponent(AppComponent.class))
                .build();
        component.inject(this);
    }

    public String getScreenNameId() {
        return AppScreen.SCREEN_IMAGE_SEARCH_TAB;
    }

    protected void reloadData() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this, this);
        return inflater.inflate(R.layout.fragment_image_search_discovery, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RedirectionListener) {
            this.redirectionListener = (RedirectionListener) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSpan();
        bindView(view);
        initTopAdsConfig();
        initTopAdsParams();
        setupAdapter();
        setupListener();
    }

    private void initTopAdsConfig() {
        topAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(userSession.getUserId())
                .setEndpoint(Endpoint.PRODUCT)
                .build();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PRODUCT_LIST, productViewModel);
        outState.putParcelable(EXTRA_SEARCH_PARAMETER, getSearchParameter());
        outState.putInt(EXTRA_SPAN_COUNT, getSpanCount());
    }

    private void setupAdapter() {
        imageProductListTypeFactory = new ImageProductListTypeFactoryImpl(this, topAdsConfig, getQueryKey());
        adapter = new ImageProductListAdapter(getActivity(), this, imageProductListTypeFactory);
        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), adapter);
        topAdsRecyclerAdapter.setConfig(topAdsConfig);
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        recyclerView.setAdapter(topAdsRecyclerAdapter);
        recyclerView.addItemDecoration(new ProductItemDecoration(
                getContext().getResources().getDimensionPixelSize(R.dimen.dp_16),
                getContext().getResources().getColor(R.color.white)
                ));
        recyclerView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        topAdsRecyclerAdapter.setLayoutManager(getGridLayoutManager());
        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                topAdsRecyclerAdapter.hideLoading();
            }
        });

        if (productViewModel.getProductList().isEmpty()) {
            setEmptyProduct();
            setHeaderTopAds(false);
        } else {
            setProductList(initMappingProduct());
            setHeaderTopAds(true);
        }
        topAdsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());
        adapter.setTotalData(productViewModel.getTotalData());
    }

    private List<Visitable> initMappingProduct() {
        List<Visitable> list = new ArrayList<>();
        HeaderViewModel headerViewModel = new HeaderViewModel();
        headerViewModel.setSuggestionModel(productViewModel.getSuggestionModel());
        list.add(headerViewModel);
        list.addAll(productViewModel.getProductList());
        return list;
    }

    private void setupListener() {
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setTopAdsListener(this);
        topAdsRecyclerAdapter.setAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                TopAdsGtmTracker.eventSearchResultProductView(getContext(), getQueryKey(), product, position);
            }
        });
    }

    protected GridLayoutManager getGridLayoutManager() {
        return gridLayoutManager;
    }

    private void bindView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getActivity(), getSpanCount());
        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup());
    }

    protected ImageProductListPresenter getPresenter() {
        return presenter;
    }

    protected GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.isEmptyItem(position) ||
                        adapter.isHeaderBanner(position) ||
                        topAdsRecyclerAdapter.isLoading(position) ||
                        topAdsRecyclerAdapter.isTopAdsViewHolder(position)) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOTO_PRODUCT_DETAIL
                && data != null
                && data.getExtras() != null
                && data.getExtras().getInt(ProductDetailRouter
                .WISHLIST_STATUS_UPDATED_POSITION, -1) != -1) {
            int position = data.getExtras().getInt(ProductDetailRouter
                    .WISHLIST_STATUS_UPDATED_POSITION, -1);
            boolean isWishlist = data.getExtras().getBoolean(ProductDetailRouter
                    .WIHSLIST_STATUS_IS_WISHLIST, false);

            String productId = data.getExtras().getString(EXTRA_PRODUCT_ID);

            if (null == productId ||
                    "".equals(productId)) {
                updateWishlistFromPDP(position, isWishlist);
            } else {
                updateWishlistFromPDP(productId, position, isWishlist);
            }
        }
    }

    private void updateWishlistFromPDP(String productId, int position, boolean isWishlist) {
        if (adapter != null && adapter.isProductItem(position)) {
            adapter.updateWishlistStatus(productId, isWishlist);
        }
    }

    private void updateWishlistFromPDP(int position, boolean isWishlist) {
        if (adapter != null && adapter.isProductItem(position)) {
            adapter.updateWishlistStatus(position, isWishlist);
        }
    }

    private void initSpan() {
        setSpanCount(calcColumnSize(getResources().getConfiguration().orientation));
    }

    private void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    private int getSpanCount() {
        return spanCount;
    }

    @Override
    public void launchLoginActivity(Bundle extras) {
        Intent intent = ((DiscoveryRouter) MainApplication.getAppContext()).getLoginIntent
                (getActivity());
        intent.putExtras(extras);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public boolean isUserHasLogin() {
        return userSession.isLoggedIn();
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
    }

    @Override
    public void initTopAdsParams() {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH);
        adsParams.getParam().put(TopAdsParams.KEY_QUERY, getQueryKey());
        if (getSearchParameter().getDepartmentId() != null &&
                !getSearchParameter().getDepartmentId().isEmpty() &&
                !getSearchParameter().getDepartmentId().equals("0")) {
            adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, getSearchParameter().getDepartmentId());
        }
    }

    @Override
    public void incrementStart() {
        adapter.incrementStart();
    }

    @Override
    public boolean isEvenPage() {
        return adapter.isEvenPage();
    }

    @Override
    public void storeTotalData(int totalData) {
        adapter.setTotalData(totalData);
    }

    @Override
    public int getStartFrom() {
        return adapter.getStartFrom();
    }

    @Override
    public void setTopAdsEndlessListener() {
        topAdsRecyclerAdapter.setEndlessScrollListener();
    }

    @Override
    public void unSetTopAdsEndlessListener() {
        topAdsRecyclerAdapter.unsetEndlessScrollListener();
        topAdsRecyclerAdapter.hideLoading();
    }

    @Override
    public void setHeaderTopAds(boolean hasHeader) {
        topAdsRecyclerAdapter.setHasHeader(hasHeader);
    }

    @Override
    public void setProductList(List<Visitable> list) {
        sendImageTrackingDataInChunks(list);
        adapter.appendItems(list);
    }

    private void sendImageTrackingDataInChunks(List<Visitable> list) {
        if (list != null && list.size() > 0) {
            String userId = userSession.isLoggedIn() ? userSession.getUserId() : "";
            List<Object> dataLayerList = new ArrayList<>();
            for (int j = 0; j < list.size(); ) {
                int count = 0;
                dataLayerList.clear();
                while (count < MAXIMUM_PRODUCT_COUNT_FOR_ONE_EVENT && j < list.size()) {
                    count++;
                    if (list.get(j) instanceof ProductItem) {
                        dataLayerList.add(((ProductItem) list.get(j)).getProductAsObjectDataLayer(userId));
                    }
                    j++;
                }
                SearchTracking.eventImpressionImageSearchResultProduct(getActivity(), dataLayerList);
            }
        }
    }

    @Override
    public void disableWishlistButton(String productId) {
        adapter.setWishlistButtonEnabled(productId, false);
    }

    @Override
    public void enableWishlistButton(String productId) {
        adapter.setWishlistButtonEnabled(productId, true);
    }


    @Override
    public void showNetworkError(final int startRow) {
        if (adapter.isListEmpty()) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    reloadData();
                }
            });
        } else {
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    adapter.setStartFrom(startRow);
                    loadMoreProduct(startRow);
                }
            }).showRetrySnackbar();
        }
    }

    private void loadMoreProduct(final int startRow) {
        SearchParameter searchParameter
                = generateLoadMoreParameter(startRow, productViewModel.getQuery());
        HashMap<String, String> additionalParams
                = NetworkParamHelper.getParamMap(productViewModel.getAdditionalParams());
        presenter.loadMoreData(searchParameter, additionalParams);
    }

    private SearchParameter generateLoadMoreParameter(int startRow, String query) {
        SearchParameter searchParameter = getSearchParameter();
        searchParameter.setUniqueID(generateUniqueId());
        searchParameter.setUserID(generateUserId());
        searchParameter.setQueryKey(query);
        searchParameter.setStartRow(startRow);
        searchParameter.setDepartmentId(getSearchParameter().getDepartmentId());
        return searchParameter;
    }

    private String generateUserId() {
        return userSession.isLoggedIn() ? userSession.getUserId() : null;
    }

    private String generateUniqueId() {
        return userSession.isLoggedIn() ?
                AuthUtil.md5(userSession.getUserId()) :
                AuthUtil.md5(gcmHandler.getRegistrationId());
    }

    @Override
    public String getQueryKey() {
        return productViewModel.getQuery();
    }

    @Override
    public void setEmptyProduct() {
        topAdsRecyclerAdapter.shouldLoadAds(false);
        adapter.showEmpty(productViewModel.getQuery());
    }

    @Override
    public SearchParameter getSearchParameter() {
        return searchParameter;
    }

    @Override
    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    @Override
    public void backToTop() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void hideRefreshLayout() {
    }

    @Override
    public void onTopAdsLoaded(List<Item> list) {

    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        topAdsRecyclerAdapter.hideLoading();
    }

    @Override
    public void onEmptyButtonClicked() {
        showSearchInputView();
    }

    private void showSearchInputView() {
        redirectionListener.showSearchInputView();
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        UnifyTracking.eventSearchResultProductWishlistClick(getActivity(),true, getQueryKey());
        adapter.updateWishlistStatus(productId, true);
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        UnifyTracking.eventSearchResultProductWishlistClick(getActivity(),false, getQueryKey());
        adapter.updateWishlistStatus(productId, false);
        enableWishlistButton(productId);
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        Intent intent = getProductIntent(product.getId());
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
    }

    private Intent getProductIntent(String productId){
        if (getContext() != null) {
            return RouteManager.getIntentInternal(getContext(),
                    UriUtil.buildUri(ApplinkConstInternal.PRODUCT_DETAIL, productId));
        } else {
            return null;
        }
    }


    @Override
    public void onShopItemClicked(int position, Shop shop) {
        Intent intent = ((DiscoveryRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shop.getId());
        startActivity(intent);
    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

    @Override
    public void onItemClicked(ProductItem item, int adapterPosition) {
        // tracking?
        //data.setTrackerListName(String.format(SearchTracking.imageClick, item.getPosition()));
        Intent intent = getProductIntent(item.getProductID());
        intent.putExtra(ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition);
        sendItemClickTrackingEvent(item);
        startActivityForResult(intent, REQUEST_CODE_GOTO_PRODUCT_DETAIL);
    }

    public void onLongClick(ProductItem item, int adapterPosition) {
        SimilarSearchManager.getInstance(getContext()).startSimilarSearchIfEnable(getQueryKey(),item);
    }
    public void onWishlistButtonClicked(ProductItem productItem) {
        presenter.handleWishlistButtonClicked(productItem);
    }

    @Override
    public void onSuggestionClicked(String suggestedQuery) {

    }

    @Override
    public void onBannerAdsClicked(String appLink) {

    }

    @Override
    public void onSearchGuideClicked(String keyword) {

    }

    @Override
    public void onRelatedSearchClicked(String keyword) {

    }

    @Override
    public void onQuickFilterSelected(Option option) {

    }

    @Override
    public void onSelectedFilterRemoved(String uniqueId) {

    }

    private void sendItemClickTrackingEvent(ProductItem item) {
        String userId = userSession.isLoggedIn() ?
                userSession.getUserId() : "";

        SearchTracking.trackEventClickImageSearchResultProduct(
                getActivity(), item.getProductAsObjectDataLayerForImageSearch(userId), (item.getPosition() + 1) / 2);
    }

    @Override
    public void onChangeList() {

    }

    @Override
    public void onChangeDoubleGrid() {

    }

    @Override
    public void onChangeSingleGrid() {

    }
}
