package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.home.helper.ProductFeedHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.transactionmodule.TransactionAddToCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.BlogWebViewActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.RecentViewActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.TransparentVideoActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.FeedPlusAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.product.AddFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.analytics.FeedTrackingEventLabel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter.FeedPlusPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareBottomDialog;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.EmptyTopAdsModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewViewModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ClientViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ShopFeedViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusFragment extends BaseDaggerFragment
        implements FeedPlus.View, FeedPlus.View.Toppicks,
        SwipeRefreshLayout.OnRefreshListener,
        TopAdsItemClickListener, TopAdsInfoClickListener, TopAdsListener {

    private static final int OPEN_DETAIL = 54;
    private static final String FIRST_CURSOR = "FIRST_CURSOR";
    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;
    RelativeLayout mainContent;
    View newFeed;

    @Inject
    FeedPlusPresenter presenter;

    private LinearLayoutManager layoutManager;
    private FeedPlusAdapter adapter;
    private ShareBottomDialog shareBottomDialog;
    private CallbackManager callbackManager;
    private TopAdsInfoBottomSheet infoBottomSheet;
    private TopAdsRecyclerAdapter topAdsRecyclerAdapter;
    private static final String TOPADS_ITEM = "4,1";
    private static final String TAG = FeedPlusFragment.class.getSimpleName();
    private String firstCursor = "";

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_HOME_PRODUCT_FEED;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerFeedPlusComponent daggerFeedPlusComponent =
                (DaggerFeedPlusComponent) DaggerFeedPlusComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerFeedPlusComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getString(FIRST_CURSOR) != null)
            firstCursor = savedInstanceState.getString(FIRST_CURSOR, "");
        initVar();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FIRST_CURSOR, firstCursor);
    }

    private void initVar() {
        FeedPlusTypeFactory typeFactory = new FeedPlusTypeFactoryImpl(this);
        adapter = new FeedPlusAdapter(typeFactory);

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .withPreferedCategory()
                .setEndpoint(Endpoint.PRODUCT)
                .displayMode(DisplayMode.FEED)
                .topAdsParams(generateTopAdsParams())
                .build();
        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), adapter);
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setTopAdsListener(this);
        topAdsRecyclerAdapter.setAdsInfoClickListener(this);
        topAdsRecyclerAdapter.setSpanSizeLookup(getSpanSizeLookup());
        topAdsRecyclerAdapter.setConfig(config);

        topAdsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                int size = adapter.getlist().size();
                int lastIndex = size - 1;
                if (!(adapter.getlist().get(0) instanceof EmptyModel)

                        && !(adapter.getlist().get(lastIndex) instanceof RetryModel)
                        && !(adapter.getlist().get(lastIndex) instanceof AddFeedViewHolder)
                        )
                    presenter.fetchNextPage();
            }
        });
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    private GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (topAdsRecyclerAdapter.isTopAdsViewHolder(position)
                        || topAdsRecyclerAdapter.isLoading(position)) {
                    return ProductFeedHelper.PORTRAIT_COLUMN_HEADER;
                } else {
                    return ProductFeedHelper.PORTRAIT_COLUMN;
                }
            }
        };
    }

    private TopAdsParams generateTopAdsParams() {
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, TopAdsParams.SRC_PRODUCT_FEED);
        params.getParam().put(TopAdsParams.KEY_ITEM, TOPADS_ITEM);
        return params;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_feed_plus, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        mainContent = (RelativeLayout) parentView.findViewById(R.id.main);
        newFeed = parentView.findViewById(R.id.layout_new_feed);

        prepareView();
        presenter.attachView(this);
        return parentView;

    }

    private void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        topAdsRecyclerAdapter.setEndlessScrollListenerVisibleThreshold(2);
        recyclerView.setAdapter(topAdsRecyclerAdapter);
        swipeToRefresh.setOnRefreshListener(this);
        infoBottomSheet = TopAdsInfoBottomSheet.newInstance(getActivity());
        newFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToTop();
                showRefresh();
                onRefresh();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    if (hasFeed()
                            && newState == RecyclerView.SCROLL_STATE_IDLE
                            && layoutManager != null
                            && topAdsRecyclerAdapter != null
                            && topAdsRecyclerAdapter.getPlacer() != null) {
                        Item item = null;
                        if (itemIsFullScreen()) {
                            item = topAdsRecyclerAdapter.getPlacer()
                                    .getItem(layoutManager.findLastVisibleItemPosition());
                        } else if (layoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                            item = topAdsRecyclerAdapter.getPlacer()
                                    .getItem(layoutManager.findFirstCompletelyVisibleItemPosition());

                        } else if (layoutManager.findLastCompletelyVisibleItemPosition() != -1) {
                            item = topAdsRecyclerAdapter.getPlacer()
                                    .getItem(layoutManager.findLastCompletelyVisibleItemPosition());
                        }

                        if (item != null && !isTopads(item)) {
                            trackImpression(item);
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.d(FeedPlusFragment.TAG, e.toString());
                }

            }

        });
    }

    private void trackImpression(Item item) {
        if (isInspirationItem(item))
            UnifyTracking.eventR3(AppEventTracking.Action.IMPRESSION,
                    FeedTrackingEventLabel.Impression.FEED_RECOMMENDATION);
        else if (isPromoItem(item)) {
            UnifyTracking.eventFeedClick(AppEventTracking.Action.IMPRESSION,
                    FeedTrackingEventLabel.Impression.FEED_PROMOTION);
        }
    }

    private boolean isPromoItem(Item item) {
        return item instanceof ClientViewModel
                && adapter.getlist().get(item.originalPos()) instanceof PromoCardViewModel;
    }

    private boolean isInspirationItem(Item item) {
        return item instanceof ClientViewModel
                && adapter.getlist().get(item.originalPos()) instanceof InspirationViewModel;
    }

    private boolean isTopads(Item item) {
        return item.originalPos() == TopAdsViewModel.TOP_ADS_POSITION_TYPE;
    }

    private boolean itemIsFullScreen() {
        return layoutManager.findLastVisibleItemPosition() -
                layoutManager.findFirstVisibleItemPosition() == 0;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.fetchFirstPage();
    }

    @Override
    public void onRefresh() {
        newFeed.setVisibility(View.GONE);
        presenter.refreshPage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        if (layoutManager != null)
            layoutManager = null;
    }

    @Override
    public void setFirstCursor(String firstCursor) {
        this.firstCursor = firstCursor;
    }

    @Override
    public void onShareButtonClicked(String shareUrl,
                                     String title,
                                     String imgUrl,
                                     String contentMessage) {

        if (shareBottomDialog == null) {
            shareBottomDialog = new ShareBottomDialog(
                    FeedPlusFragment.this,
                    callbackManager);
        }

        shareBottomDialog.setShareModel(
                new ShareModel(shareUrl,
                        title,
                        imgUrl,
                        contentMessage));

        shareBottomDialog.show();
    }

    private void goToProductDetail(String productId) {
        if (getActivity().getApplication() instanceof PdpRouter) {
            ((PdpRouter) getActivity().getApplication()).goToProductDetail(getActivity(), productId);
        }
    }

    @Override
    public void onGoToProductDetail(int page, String productId) {
        goToProductDetail(productId);
        UnifyTracking.eventFeedViewProduct(productId, FeedTrackingEventLabel.View.FEED_PDP);
    }

    @Override
    public void onGoToProductDetailFromRecentView(String productId) {
        goToProductDetail(productId);
        UnifyTracking.eventFeedViewProduct(productId, FeedTrackingEventLabel.View.VIEW_RECENT,
                FeedTrackingEventLabel.View.FEED_PDP);
    }

    @Override
    public void onGoToProductDetailFromInspiration(String productId) {
        goToProductDetail(productId);
        UnifyTracking.eventR3Product(productId, AppEventTracking.Action.CLICK,
                FeedTrackingEventLabel.Click.FEED_RECOMMENDATION_PDP);
    }


    @Override
    public void onGoToFeedDetail(int page, String feedId) {
        Intent intent = FeedPlusDetailActivity.getIntent(
                getActivity(),
                feedId);
        startActivityForResult(intent, OPEN_DETAIL);
        UnifyTracking.eventFeedView(FeedTrackingEventLabel.View.FEED_PRODUCT_LIST);

    }

    @Override
    public void onGoToShopDetail(Integer shopId, String url) {
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        Bundle bundle = ShopInfoActivity.createBundle(String.valueOf(shopId), "");
        intent.putExtras(bundle);
        startActivity(intent);
        UnifyTracking.eventFeedViewShop(String.valueOf(shopId), FeedTrackingEventLabel.View.FEED_SHOP);

    }

    @Override
    public void onCopyClicked(String id, String code, String name) {
        ClipboardHandler.CopyToClipboard(getActivity(), code);
        SnackbarManager.make(getActivity(), getResources().getString(R.string.copy_promo_success),
                Snackbar.LENGTH_SHORT).show();
        UnifyTracking.eventFeedClickPromo(id, AppEventTracking.Action.COPY_CODE,
                FeedTrackingEventLabel.Click.PROMO_COPY + name);

    }

    @Override
    public void onGoToBlogWebView(String url) {
        Intent intent = BlogWebViewActivity.getIntent(getActivity(), url);
        startActivity(intent);
    }

    @Override
    public void onOpenVideo(String videoUrl, String subtitle) {
        Intent intent = TransparentVideoActivity.getIntent(getActivity(), videoUrl, subtitle);
        startActivity(intent);
    }

    @Override
    public void onGoToBuyProduct(ProductFeedViewModel productFeedViewModel) {

        ProductCartPass pass = ProductCartPass.Builder.aProductCartPass()
                .setProductId(String.valueOf(productFeedViewModel.getProductId()))
                .setPrice(productFeedViewModel.getPrice())
                .setImageUri(productFeedViewModel.getImageSource())
                .build();

        Intent intent = TransactionAddToCartRouter
                .createInstanceAddToCartActivity(getActivity(), pass);
        startActivity(intent);

    }

    @Override
    public void onInfoClicked() {
        infoBottomSheet.show();
    }

    @Override
    public void onFavoritedClicked(int adapterPosition) {
        adapter.getItemViewType(adapterPosition);
    }

    @Override
    public void showSnackbar(String s) {
        SnackbarManager.make(getActivity(), s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateFavorite(int adapterPosition) {
        Object item = ((TopAdsViewModel) topAdsRecyclerAdapter.getPlacer().getItem(adapterPosition)).getList().get(0);
        if (item instanceof ShopFeedViewModel) {
            ShopFeedViewModel castedItem = ((ShopFeedViewModel) item);
            Data currentData = castedItem.getData();
            boolean currentStatus = currentData.isFavorit();
            currentData.setFavorit(!currentStatus);
            topAdsRecyclerAdapter.notifyItemChanged(adapterPosition);
        }
    }

    @Override
    public void onViewMorePromoClicked() {
        goToAllPromo();
        UnifyTracking.eventFeedClick(FeedTrackingEventLabel.Click.PROMO_MORE);

    }

    private void goToAllPromo() {
        Intent intent = BlogWebViewActivity.getIntent(getActivity(), TkpdBaseURL.URL_PROMO +
                TkpdBaseURL.FLAG_APP);
        startActivity(intent);
    }


    @Override
    public void onSuccessGetFeedFirstPage(ArrayList<Visitable> listFeed) {
        topAdsRecyclerAdapter.reset();

        adapter.setList(listFeed);
        adapter.notifyDataSetChanged();
        if(listFeed.get(0) instanceof RecentViewViewModel){
            topAdsRecyclerAdapter.setHasHeader(true);
        } else {
            topAdsRecyclerAdapter.setHasHeader(false);
        }
        topAdsRecyclerAdapter.setEndlessScrollListener();
    }

    @Override
    public void onSuccessGetFeedFirstPageWithAddFeed(ArrayList<Visitable> listFeed) {
        topAdsRecyclerAdapter.reset();
        topAdsRecyclerAdapter.shouldLoadAds(true);
        if(listFeed.get(0) instanceof RecentViewViewModel){
            topAdsRecyclerAdapter.setHasHeader(true);
        } else {
            topAdsRecyclerAdapter.setHasHeader(false);
        }
        adapter.setList(listFeed);
        adapter.notifyDataSetChanged();
        int positionStart = adapter.getItemCount();
        adapter.showAddFeed();
        adapter.notifyItemRangeInserted(positionStart, 1);
        topAdsRecyclerAdapter.unsetEndlessScrollListener();
    }

    @Override
    public void onShowEmptyWithRecentView(ArrayList<Visitable> listFeed, boolean canShowTopads) {
        topAdsRecyclerAdapter.reset();
        topAdsRecyclerAdapter.setHasHeader(true);
        topAdsRecyclerAdapter.shouldLoadAds(false);
        topAdsRecyclerAdapter.unsetEndlessScrollListener();

        adapter.showEmpty();
        adapter.addList(listFeed);
        if (canShowTopads)
            adapter.addItem(new EmptyTopAdsModel(presenter.getUserId()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onShowEmpty(boolean canShowTopads) {
        topAdsRecyclerAdapter.shouldLoadAds(false);
        topAdsRecyclerAdapter.unsetEndlessScrollListener();

        adapter.showEmpty();
        if (canShowTopads)
            adapter.addItem(new EmptyTopAdsModel(presenter.getUserId()));
        adapter.notifyDataSetChanged();

    }

    @Override
    public void clearData() {
        adapter.clearData();
        topAdsRecyclerAdapter.reset();
    }

    @Override
    public void unsetEndlessScroll() {
        topAdsRecyclerAdapter.unsetEndlessScrollListener();
    }

    @Override
    public void onShowNewFeed(String totalData) {
        newFeed.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGoToPromoPageFromHeader() {
        goToAllPromo();
        UnifyTracking.eventFeedClick(FeedTrackingEventLabel.Click.PROMO_PAGE_HEADER);

    }

    @Override
    public void onHideNewFeed() {
        newFeed.setVisibility(View.GONE);
    }

    @Override
    public void finishLoading() {
        swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onErrorGetFeedFirstPage(String errorMessage) {
        finishLoading();
        if (adapter.getItemCount() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), mainContent, errorMessage,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.refreshPage();
                        }
                    });
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        }

    }

    @Override
    public void onSearchShopButtonClicked() {
        Intent intent = HomeRouter.getHomeActivity(getActivity());
        intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_FAVORITE);
        startActivity(intent);
    }

    @Override
    public void showRefresh() {
        if (!swipeToRefresh.isRefreshing()) {
            swipeToRefresh.setRefreshing(true);
        }
    }

    @Override
    public void updateCursor(String currentCursor) {
        presenter.setCursor(currentCursor);
    }


    @Override
    public void onSuccessGetFeed(ArrayList<Visitable> listFeed) {
        adapter.removeEmpty();
        int posStart = adapter.getItemCount();
        adapter.addList(listFeed);
        adapter.notifyItemRangeInserted(posStart, listFeed.size());
    }

    @Override
    public void onShowRetryGetFeed() {
        adapter.showRetry();
    }

    @Override
    public void onShowAddFeedMore() {
        topAdsRecyclerAdapter.shouldLoadAds(false);
        int positionStart = adapter.getItemCount();
        adapter.showAddFeed();
        adapter.notifyItemRangeInserted(positionStart, 1);
    }

    @Override
    public void shouldLoadTopAds(boolean loadTopAds) {
        topAdsRecyclerAdapter.shouldLoadAds(loadTopAds);
        topAdsRecyclerAdapter.unsetEndlessScrollListener();
    }

    @Override
    public void hideTopAdsAdapterLoading() {
        topAdsRecyclerAdapter.hideLoading();
    }

    @Override
    public int getColor(int color) {
        return MethodChecker.getColor(getActivity(), color);
    }

    @Override
    public void onSeeAllRecentView() {
        Intent intent = RecentViewActivity.getCallingIntent(getActivity());
        getActivity().startActivity(intent);
    }


    @Override
    public void onSeePromo(String id, String link, String name) {
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), link);
        UnifyTracking.eventFeedClickPromo(id, FeedTrackingEventLabel.Click.PROMO_SPECIFIC + name);
    }

    @Override
    public void onRetryClicked() {
        adapter.removeRetry();
        topAdsRecyclerAdapter.showLoading();
        topAdsRecyclerAdapter.shouldLoadAds(true);
        topAdsRecyclerAdapter.setEndlessScrollListener();
        presenter.fetchNextPage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_DETAIL:
                if (resultCode == Activity.RESULT_OK)
                    showSnackbar(data.getStringExtra("message"));
                break;
            default:
                break;
        }
    }

    @Override
    public void onProductItemClicked(Product product) {
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity(),
                product.getId());
        getActivity().startActivity(intent);
        UnifyTracking.eventFeedClickProduct(product.getId(),
                FeedTrackingEventLabel.Click.TOP_ADS_PRODUCT);

    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Bundle bundle = ShopInfoActivity.createBundle(shop.getId(), "");
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
        UnifyTracking.eventFeedClickShop(shop.getId(), FeedTrackingEventLabel.Click.TOP_ADS_SHOP);

    }

    @Override
    public void onAddFavorite(int position, Data dataShop) {
        presenter.favoriteShop(dataShop, position);
        UnifyTracking.eventFeedClickShop(dataShop.getShop().getId(),
                FeedTrackingEventLabel.Click.TOP_ADS_FAVORITE);

    }

    @Override
    public void onTopAdsLoaded() {
        hideTopAdsAdapterLoading();
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        hideTopAdsAdapterLoading();
    }

    public void scrollToTop() {
        if (recyclerView != null) recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstCursor == null)
            firstCursor = "";
        if (getUserVisibleHint() && presenter != null) {
            presenter.checkNewFeed(firstCursor);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (firstCursor == null)
            firstCursor = "";
        if (isVisibleToUser && presenter != null) {
            presenter.checkNewFeed(firstCursor);
        }
    }

    @Override
    public boolean hasFeed() {
        return adapter.getlist() != null
                && !adapter.getlist().isEmpty()
                && adapter.getlist().size() > 1
                && !(adapter.getlist().get(0) instanceof EmptyModel);
    }

    @Override
    public void updateFavoriteFromEmpty(String shopId) {
        onRefresh();
        UnifyTracking.eventFeedClickShop(shopId, FeedTrackingEventLabel.Click.
                TOP_ADS_FAVORITE);

    }

    @Override
    public void showTopAds(boolean isTopAdsShown) {
        topAdsRecyclerAdapter.shouldLoadAds(isTopAdsShown);
    }

    @Override
    public void onEmptyOfficialStoreClicked() {
        openWebViewBrandsURL(TkpdBaseURL.OfficialStore.URL_WEBVIEW);
    }

    @Override
    public void onBrandClicked(OfficialStoreViewModel officialStoreViewModel) {
        UnifyTracking.eventFeedClickShop(
                String.valueOf(officialStoreViewModel.getShopId()),
                FeedTrackingEventLabel.Click
                        .OFFICIAL_STORE_BRAND +
                        officialStoreViewModel.getShopName());

        Intent intent = ShopInfoActivity.getCallingIntent(
                getActivity(), String.valueOf(officialStoreViewModel.getShopId()));
        getActivity().startActivity(intent);
    }

    @Override
    public void onSeeAllOfficialStoresFromCampaign(String redirectUrl) {
        UnifyTracking.eventFeedClick(FeedTrackingEventLabel.Click.OFFICIAL_STORE_CAMPAIGN_SEE_ALL);
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), redirectUrl);
    }

    @Override
    public void onGoToCampaign(String redirectUrl, String title) {
        UnifyTracking.eventFeedClick(FeedTrackingEventLabel.Click.OFFICIAL_STORE_CAMPAIGN + title);
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), redirectUrl);

    }

    @Override
    public void onSeeAllOfficialStoresFromBrands() {
        UnifyTracking.eventFeedClick(FeedTrackingEventLabel.Click.OFFICIAL_STORE_BRAND_SEE_ALL);
        openWebViewBrandsURL(TkpdBaseURL.OfficialStore.URL_WEBVIEW);
    }

    @Override
    public void onGoToProductDetailFromCampaign(String productId) {
        UnifyTracking.eventFeedClickProduct(
                productId,
                FeedTrackingEventLabel.Click.OFFICIAL_STORE_CAMPAIGN_PDP);
        goToProductDetail(productId);

    }

    @Override
    public void onGoToShopDetailFromCampaign(String shopUrl) {
        UnifyTracking.eventFeedClick(FeedTrackingEventLabel.Click.OFFICIAL_STORE_CAMPAIGN_SHOP);
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), shopUrl);
    }

    private void openWebViewBrandsURL(String url) {
        if (!url.trim().equals("")) {
            startActivity(BrandsWebViewActivity.newInstance(getActivity(), url));
        }
    }


    @Override
    public void onToppicksClicked(String name, String url) {
        UnifyTracking.eventFeedClick(FeedTrackingEventLabel.Click.TOPPICKS + name);
        switch ((DeepLinkChecker.getDeepLinkType(url))) {
            case DeepLinkChecker.BROWSE:
                DeepLinkChecker.openBrowse(url, getActivity());
                break;
            case DeepLinkChecker.HOT:
                DeepLinkChecker.openHot(url, getActivity());
                break;
            case DeepLinkChecker.CATALOG:
                DeepLinkChecker.openCatalog(url, getActivity());
                break;
            default:
                if (!TextUtils.isEmpty(url)) {
                    ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity()
                            , url);
                }
        }
    }

    @Override
    public void onSeeAllToppicks() {
        startActivity(TopPicksWebView.newInstance(getActivity(), TkpdBaseURL.URL_TOPPICKS));
        UnifyTracking.eventFeedClick(FeedTrackingEventLabel.Click.TOPPICKS_SEE_ALL);
    }

}
