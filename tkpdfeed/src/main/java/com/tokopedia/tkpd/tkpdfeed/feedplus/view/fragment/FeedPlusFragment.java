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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.helper.ProductFeedHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.transactionmodule.TransactionAddToCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.BlogWebViewActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.TransparentVideoActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.FeedPlusAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.AddFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter.FeedPlusPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareBottomDialog;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromoCardViewModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsFavShopClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopFeedViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.TopAdsViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusFragment extends BaseDaggerFragment
        implements FeedPlus.View,
        SwipeRefreshLayout.OnRefreshListener,
        TopAdsItemClickListener, TopAdsInfoClickListener, TopAdsListener,
        TopAdsFavShopClickListener {

    private static final int OPEN_DETAIL = 54;
    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;

    @Inject
    FeedPlusPresenter presenter;

    private LinearLayoutManager layoutManager;
    private FeedPlusAdapter adapter;
    private ShareBottomDialog shareBottomDialog;
    private CallbackManager callbackManager;
    private TopAdsInfoBottomSheet infoBottomSheet;
    private TopAdsRecyclerAdapter topAdsRecyclerAdapter;
    private static final String TOPADS_ITEM = "4";
    private static final String TAG = FeedPlusFragment.class.getSimpleName();

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
        initVar();
    }

    private void initVar() {
        FeedPlusTypeFactory typeFactory = new FeedPlusTypeFactoryImpl(this);
        adapter = new FeedPlusAdapter(typeFactory);

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .withPreferedCategory()
                .setEndpoint(Endpoint.RANDOM)
                .displayMode(DisplayMode.FEED)
                .topAdsParams(generateTopAdsParams())
                .build();
        topAdsRecyclerAdapter = new TopAdsRecyclerAdapter(getActivity(), adapter);
        topAdsRecyclerAdapter.setAdsItemClickListener(this);
        topAdsRecyclerAdapter.setTopAdsListener(this);
        topAdsRecyclerAdapter.setAdsInfoClickListener(this);
        topAdsRecyclerAdapter.setFavShopClickListener(this);
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
        View parentView = inflater.inflate(R.layout.fragment_feed_plus, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.fetchFirstPage();
    }

    @Override
    public void onRefresh() {
        presenter.refreshPage();
        topAdsRecyclerAdapter.reset();
        topAdsRecyclerAdapter.shouldLoadAds(true);
        topAdsRecyclerAdapter.setEndlessScrollListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
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

    @Override
    public void onGoToProductDetail(String productId) {
        if (getActivity().getApplication() instanceof PdpRouter) {
            ((PdpRouter) getActivity().getApplication()).goToProductDetail(getActivity(), productId);
        }
    }


    @Override
    public void onGoToFeedDetail(String feedId) {
        Intent intent = FeedPlusDetailActivity.getIntent(
                getActivity(),
                feedId);
        startActivityForResult(intent, OPEN_DETAIL);
    }

    @Override
    public void onGoToShopDetail(Integer shopId, String url) {
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        Bundle bundle = ShopInfoActivity.createBundle(String.valueOf(shopId), url);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCopyClicked(String s) {
        ClipboardHandler.CopyToClipboard(getActivity(), s);
        SnackbarManager.make(getActivity(), getResources().getString(R.string.copy_promo_success), Snackbar.LENGTH_SHORT).show();
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
        Intent intent = BlogWebViewActivity.getIntent(getActivity(), "https://www.tokopedia.com/promo/");
        startActivity(intent);
    }


    @Override
    public void onSuccessGetFeedFirstPage(ArrayList<Visitable> listFeed) {
        finishLoading();
        adapter.clearData();
        adapter.removeEmpty();
        if ((listFeed.size() == 0) ||
                (listFeed.size() == 1 && listFeed.get(0) instanceof PromoCardViewModel)) {
            adapter.showEmpty();
            topAdsRecyclerAdapter.unsetEndlessScrollListener();
        } else {
            adapter.setList(listFeed);
        }
        adapter.notifyDataSetChanged();
    }

    private void finishLoading() {
        if (swipeToRefresh.isRefreshing())
            swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onErrorGetFeedFirstPage(String errorMessage) {
        finishLoading();
        if (adapter.getItemCount() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage,
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
        adapter.addList(listFeed);
    }


    @Override
    public void onShowRetryGetFeed() {
        adapter.showRetry();
    }

    @Override
    public void onShowAddFeedMore() {
        adapter.showAddFeed();
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
    public void onSeePromo(String link) {
        ((TkpdCoreRouter) getActivity().getApplication()).actionAppLink(getActivity(), link);
    }

    @Override
    public void onErrorGetFeed() {
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
    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Bundle bundle = ShopInfoActivity.createBundle(shop.getId(), "");
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(Data dataShop) {
        Log.d(TAG, "onAddFavorite " + dataShop.getShop().getName());

    }

    @Override
    public void onTopAdsLoaded() {
        hideTopAdsAdapterLoading();
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        hideTopAdsAdapterLoading();
    }

    @Override
    public void onAddShopFavorite(int position, Data data) {
        presenter.favoriteShop(data, position);
    }

    public void scrollToTop() {
        if(recyclerView != null) recyclerView.smoothScrollToPosition(0);
    }
}
