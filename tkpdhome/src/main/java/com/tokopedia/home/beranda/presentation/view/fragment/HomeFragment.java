package com.tokopedia.home.beranda.presentation.view.fragment;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.perf.metrics.Trace;
import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.constants.HomeFragmentBroadcastReceiverConstant;
import com.tokopedia.core.constants.TokocashPendingDataBroadcastReceiverConstant;
import com.tokopedia.core.drawer.listener.TokoCashUpdateListener;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.home.TopPicksWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.home.R;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.sellermodule.TokoCashRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.loyalty.view.activity.TokoPointWebviewActivity;
import com.tokopedia.home.beranda.di.BerandaComponent;
import com.tokopedia.home.beranda.di.DaggerBerandaComponent;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.listener.HomeFeedListener;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.SectionContainer;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter;
import com.tokopedia.home.beranda.presentation.view.adapter.LinearLayoutManagerWithSmoothScroller;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.constants.HomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER;

/**
 * @author by errysuprayogi on 11/27/17.
 */
@RuntimePermissions
public class HomeFragment extends BaseDaggerFragment implements HomeContract.View,
        SwipeRefreshLayout.OnRefreshListener, HomeCategoryListener,
        TokoCashUpdateListener, HomeFeedListener {

    @Inject
    HomePresenter presenter;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String MAINAPP_SHOW_REACT_OFFICIAL_STORE = "mainapp_react_show_os";
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private CoordinatorLayout root;
    private SectionContainer tabContainer;
    private SwipeRefreshLayout refreshLayout;
    private HomeRecycleAdapter adapter;
    private RemoteConfig firebaseRemoteConfig;
    private Trace trace;
    private SnackbarRetry messageSnackbar;
    private HomeAdapterFactory adapterFactory;
    private String[] tabSectionTitle;
    private HomeFragmentBroadcastReceiver homeFragmentBroadcastReceiver;
    private EndlessRecyclerviewListener feedLoadMoreTriggerListener;
    private LinearLayoutManager layoutManager;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        trace = TrackingUtils.startTrace("beranda_trace");
        super.onCreate(savedInstanceState);


        homeFragmentBroadcastReceiver = new HomeFragmentBroadcastReceiver();
        getActivity().registerReceiver(
                homeFragmentBroadcastReceiver,
                new IntentFilter(
                        HomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP
                )
        );
    }

    @Override
    protected String getScreenName() {
        return AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_BERANDA;
    }

    @Override
    protected void initInjector() {
        BerandaComponent component = DaggerBerandaComponent.builder().baseAppComponent(((BaseMainApplication)
                getActivity().getApplication()).getBaseAppComponent()).build();
        component.inject(this);
        component.inject(presenter);
    }

    private void fetchRemoteConfig() {
        firebaseRemoteConfig = new FirebaseRemoteConfigImpl(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.list);
        refreshLayout = view.findViewById(R.id.sw_refresh_layout);
        tabLayout = view.findViewById(R.id.tabs);
        tabContainer = view.findViewById(R.id.tab_container);
        root = view.findViewById(R.id.root);
        presenter.attachView(this);
        presenter.setFeedListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (trace != null)
            trace.stop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTabNavigation();
        initAdapter();
        initRefreshLayout();
        initFeedLoadMoreTriggerListener();
        fetchRemoteConfig();
    }

    private void initTabNavigation() {
        tabSectionTitle = getResources().getStringArray(R.array.section_title);
        TypedArray icons = getResources().obtainTypedArray(R.array.section_icon);
        for (int i = 0; i < tabSectionTitle.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setIcon(icons.getResourceId(i, R.drawable.ic_beli));
            tabLayout.addTab(tab);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(homeFragmentBroadcastReceiver);
        presenter.detachView();
    }

    private void initRefreshLayout() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                presenter.getHomeData();
                presenter.getHeaderData(true);
            }
        });
        refreshLayout.setOnRefreshListener(this);
    }

    private void initFeedLoadMoreTriggerListener() {
        feedLoadMoreTriggerListener = new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isAllowLoadMore()) {
                    adapter.showLoading();
                    presenter.fetchNextPageFeed();
                }
            }
        };

        if (SessionHandler.isV4Login(getContext())) {
            recyclerView.addOnScrollListener(feedLoadMoreTriggerListener);
        }
    }

    private boolean isAllowLoadMore() {
        return getUserVisibleHint()
                && presenter.hasNextPageFeed()
                && !adapter.isLoading()
                && !adapter.isRetryShown()
                && !refreshLayout.isRefreshing()
                && !isErrorMessageShown();
    }

    private boolean isErrorMessageShown() {
        return messageSnackbar != null && messageSnackbar.isShown();
    }

    private void initAdapter() {
        layoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapterFactory = new HomeAdapterFactory(getFragmentManager(), this, this);
        adapter = new HomeRecycleAdapter(adapterFactory, new ArrayList<Visitable>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSectionItemClicked(String actionLink) {
        onActionLinkClicked(actionLink);
    }

    private void onGoToSell() {
        if (SessionHandler.isV2Login(getContext())) {
            String shopId = SessionHandler.getShopID(getContext());
            if (!shopId.equals("0")) {
                onGoToShop(shopId);
            } else {
                onGoToCreateShop();
            }
        } else {
            onGoToLogin();
        }
    }

    private void onGoToLogin() {
        Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).getLoginIntent(getContext());
        Intent intentHome = ((TkpdCoreRouter) getActivity().getApplication()).getHomeIntent(getContext());
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivities(new Intent[]{intentHome, intent});
        getActivity().finish();
    }

    private void onGoToCreateShop() {
        Intent intent = SellerRouter.getActivityShopCreateEdit(getContext());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private void onGoToShop(String shopId) {
        Intent intent = new Intent(getContext(), ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(shopId, ""));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }


    @Override
    public void onDigitalMoreClicked(int pos) {
        if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
            IDigitalModuleRouter digitalModuleRouter =
                    (IDigitalModuleRouter) getActivity().getApplication();
            startActivityForResult(
                    digitalModuleRouter.instanceIntentDigitalCategoryList(),
                    IDigitalModuleRouter.REQUEST_CODE_DIGITAL_CATEGORY_LIST
            );
        }
    }

    @Override
    public void openShop() {
        onGoToSell();
    }

    @Override
    public void actionAppLinkWalletHeader(String redirectUrlBalance, String appLinkBalance) {
        WalletRouterUtil.navigateWallet(
                getActivity().getApplication(),
                this,
                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                appLinkBalance,
                redirectUrlBalance,
                new Bundle()
        );
    }

    @Override
    public void onRequestPendingCashBack() {
        getActivity().sendBroadcast(new Intent(TokocashPendingDataBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP));
    }

    @Override
    public void actionInfoPendingCashBackTokocash(CashBackData cashBackData,
                                                  String redirectUrlActionButton,
                                                  String appLinkActionButton) {
        BottomSheetView bottomSheetDialogTokoCash = new BottomSheetView(getActivity());
        bottomSheetDialogTokoCash.setListener(new BottomSheetView.ActionListener() {
            @Override
            public void clickOnTextLink(String url) {

            }

            @Override
            public void clickOnButton(String url, String appLink) {
                if (TextUtils.isEmpty(appLink)) {
                    String seamlessUrl;
                    seamlessUrl = URLGenerator.generateURLSessionLogin((Uri.encode(url)),
                            getContext());
                    if (getActivity() != null) {
                        if ((getActivity()).getApplication() instanceof TkpdCoreRouter) {
                            ((TkpdCoreRouter) (getActivity()).getApplication())
                                    .goToWallet(getActivity(), seamlessUrl);
                        }
                    }
                } else {
                    WalletRouterUtil.navigateWallet(
                            getActivity().getApplication(),
                            HomeFragment.this,
                            IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                            appLink, url, new Bundle()
                    );
                }

            }
        });
        bottomSheetDialogTokoCash.renderBottomSheet(new BottomSheetView
                .BottomSheetField.BottomSheetFieldBuilder()
                .setTitle(getString(R.string.toko_cash_pending_title))
                .setBody(String.format(getString(R.string.toko_cash_pending_body),
                        cashBackData.getAmountText()))
                .setImg(R.drawable.ic_box)
                .setUrlButton(redirectUrlActionButton,
                        appLinkActionButton,
                        getString(R.string.toko_cash_pending_proceed_button))
                .build());
        bottomSheetDialogTokoCash.show();
    }

    @Override
    public void actionTokoPointClicked(String tokoPointUrl, String pageTitle) {
        if (TextUtils.isEmpty(pageTitle))
            startActivity(TokoPointWebviewActivity.getIntent(getActivity(), tokoPointUrl));
        else
            startActivity(TokoPointWebviewActivity.getIntentWithTitle(getActivity(), tokoPointUrl, pageTitle));
    }


    @Override
    public void actionScannerQRTokoCash() {
        HomeFragmentPermissionsDispatcher.scanQRCodeWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void scanQRCode() {
        Application application = getActivity().getApplication();
        if (application != null && application instanceof TokoCashRouter) {
            Intent intent = ((TokoCashRouter) application).goToQRScannerTokoCash(getActivity());
            startActivity(intent);
        }
    }

    @Override
    public void onPromoClick(BannerSlidesModel slidesModel) {
        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof TkpdCoreRouter
                && ((TkpdCoreRouter) getActivity().getApplicationContext()).isSupportedDelegateDeepLink(slidesModel.getApplink())) {
            openApplink(slidesModel.getApplink());
        } else {
            openWebViewURL(slidesModel.getRedirectUrl(), getContext());
        }
    }

    @Override
    public void onCloseTicker(int pos) {
        adapter.getItems().remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_DETAIL:
                if (data != null && data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                    String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                    if (!TextUtils.isEmpty(message)) {
                        NetworkErrorHelper.showSnackbar(getActivity(), message);
                    }
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        removeNetworkError();
        resetFeedState();
        presenter.getHomeData();
        presenter.getHeaderData(false);
    }

    private void resetFeedState() {
        presenter.resetPageFeed();
        if (SessionHandler.isV4Login(getContext()) && feedLoadMoreTriggerListener != null) {
            feedLoadMoreTriggerListener.resetState();
        }
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void setItems(List<Visitable> items) {
        adapter.setItems(items);
    }

    @Override
    public void updateHeaderItem(HeaderViewModel headerViewModel) {
        if (adapter.getItemCount() > 0 && adapter.getItem(0) instanceof HeaderViewModel) {
            adapter.getItems().set(0, headerViewModel);
            adapter.notifyItemChanged(0);
        }
    }

    @Override
    public void showNetworkError(String message) {
        if (isAdded() && getActivity() != null) {
            if (adapter.getItemCount() > 0) {
                if (messageSnackbar == null) {
                    messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            onRefresh();
                        }
                    });
                }
                messageSnackbar.showRetrySnackbar();
            } else {
                NetworkErrorHelper.showEmptyState(getActivity(), root, message,
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                onRefresh();
                            }
                        });
            }
        }
    }

    @Override
    public void onDynamicChannelClicked(String actionLink) {
        onActionLinkClicked(actionLink);
    }

    private void onActionLinkClicked(String actionLink) {
        if (TextUtils.isEmpty(actionLink)) {
            return;
        }

        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof TkpdCoreRouter
                && ((TkpdCoreRouter) getActivity().getApplicationContext()).isSupportedDelegateDeepLink(actionLink)) {
            openApplink(actionLink);
        } else {
            openWebViewURL(actionLink, getContext());
        }
    }

    private void openApplink(String applink) {
        if (!TextUtils.isEmpty(applink)) {
            ((TkpdCoreRouter) getActivity().getApplicationContext())
                    .actionApplink(getActivity(), applink);
        }
    }

    @Override
    public void removeNetworkError() {
        NetworkErrorHelper.removeEmptyState(root);
        if (messageSnackbar != null && messageSnackbar.isShown()) {
            messageSnackbar.hideRetrySnackbar();
            messageSnackbar = null;
        }
    }

    @Override
    public void onGoToProductDetailFromInspiration(String productId,
                                                   String imageSource,
                                                   String name,
                                                   String price) {
        goToProductDetail(productId, imageSource, name, price);
    }

    private void goToProductDetail(String productId, String imageSourceSingle, String name, String price) {
        if (getActivity().getApplication() instanceof PdpRouter) {
            ((PdpRouter) getActivity().getApplication()).goToProductDetail(
                    getActivity(),
                    ProductPass.Builder.aProductPass()
                            .setProductId(productId)
                            .setProductImage(imageSourceSingle)
                            .setProductName(name)
                            .setProductPrice(price)
                            .build()
            );
        }
    }

    @Override
    public void updateCursor(String currentCursor) {
        presenter.setCursor(currentCursor);
    }

    @Override
    public void onSuccessGetFeed(ArrayList<Visitable> visitables) {
        adapter.hideLoading();
        int posStart = adapter.getItemCount();
        adapter.addItems(visitables);
        adapter.notifyItemRangeInserted(posStart, visitables.size());
    }

    @Override
    public void onRetryClicked() {
        if (!isErrorMessageShown()) {
            adapter.removeRetry();
            adapter.showLoading();
            presenter.fetchCurrentPageFeed();
        } else {
            onRefresh();
        }
    }

    @Override
    public void onShowRetryGetFeed() {
        adapter.hideLoading();
        adapter.showRetry();
    }

    @Override
    public void updateCursorNoNextPageFeed() {
        presenter.setCursorNoNextPageFeed();
    }

    private void openWebViewBrandsURL(String url) {
        if (!url.trim().equals("")) {
            startActivity(BrandsWebViewActivity.newInstance(getActivity(), url));
        }
    }

    public void openWebViewTopPicksURL(String url) {
        if (!url.isEmpty()) {
            startActivity(TopPicksWebView.newInstance(getActivity(), url));
        }
    }

    private void openWebViewGimicURL(String url, String label, String title) {
        if (!url.equals("")) {
            Intent intent = SimpleWebViewWithFilePickerActivity.getIntentWithTitle(getActivity(), url, title);
            startActivity(intent);
            UnifyTracking.eventHomeGimmick(label);
        }
    }


    public void openWebViewURL(String url, Context context) {
        if (!TextUtils.isEmpty(url) && context != null) {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

    @Override
    public void onReceivedTokoCashData(DrawerTokoCash tokoCashData) {
        presenter.updateHeaderTokoCashData(tokoCashData.getHomeHeaderWalletAction());
    }

    @Override
    public void onTokoCashDataError(String errorMessage) {
        Log.e(TAG, errorMessage);
    }

    public class HomeFragmentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!HomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP.equalsIgnoreCase(intent.getAction()))
                return;
            switch (intent.getIntExtra(EXTRA_ACTION_RECEIVER, 0)) {
                case HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA:
                    TokoPointDrawerData tokoPointDrawerData = intent.getParcelableExtra(
                            HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOPOINT_DRAWER_DATA
                    );
                    if (tokoPointDrawerData != null)
                        presenter.updateHeaderTokoPointData(tokoPointDrawerData);
                    break;
                case HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA:
                    HomeHeaderWalletAction homeHeaderWalletAction = intent.getParcelableExtra(
                            HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOCASH_DRAWER_DATA
                    );
                    if (homeHeaderWalletAction != null)
                        presenter.updateHeaderTokoCashData(homeHeaderWalletAction);
                    break;
                case HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_PENDING_DATA:
                    CashBackData cashBackData = intent.getParcelableExtra(
                            HomeFragmentBroadcastReceiverConstant.EXTRA_TOKOCASH_PENDING_DATA
                    );
                    if (cashBackData != null)
                        presenter.updateHeaderTokoCashPendingData(cashBackData);
                    break;
                case HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA_ERROR:
                    presenter.updateHeaderTokoCashData(null);
                    break;
                case HomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOPOINT_DATA_ERROR:
                    presenter.updateHeaderTokoPointData(null);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        trackScreen(isVisibleToUser);
        restartBanner(isVisibleToUser);
    }

    private void restartBanner(boolean isVisibleToUser) {
        if ((isVisibleToUser && getView() != null) && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void trackScreen(boolean isVisibleToUser) {
        if (isVisibleToUser && isAdded() && getActivity() != null) {
            ScreenTracking.screen(getScreenName());
        }
    }

    @Override
    public boolean isMainViewVisible() {
        return getUserVisibleHint();
    }

    public void scrollToTop() {
        if (recyclerView != null) recyclerView.scrollToPosition(0);
    }
}
