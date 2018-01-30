package com.tokopedia.loyalty.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.R2;
import com.tokopedia.loyalty.di.component.DaggerPromoListFragmentComponent;
import com.tokopedia.loyalty.di.component.PromoListFragmentComponent;
import com.tokopedia.loyalty.di.module.PromoListFragmentModule;
import com.tokopedia.loyalty.view.adapter.PromoListAdapter;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.data.PromoSubMenuData;
import com.tokopedia.loyalty.view.presenter.IPromoListPresenter;
import com.tokopedia.loyalty.view.view.IPromoListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoListFragment extends BasePresenterFragment implements IPromoListView,
        PromoListAdapter.ActionListener, RefreshHandler.OnRefreshHandlerListener {
    private static final String ARG_EXTRA_PROMO_MENU_DATA = "ARG_EXTRA_PROMO_MENU_DATA";

    private static final String EXTRA_STATE_PROMO_MENU_DATA = "EXTRA_STATE_PROMO_MENU_DATA";
    private static final String EXTRA_STATE_FILTER_SELECTED = "EXTRA_STATE_FILTER_SELECTED";

    private static final String TYPE_FILTER_ALL = "all";
    @BindView(R2.id.quick_filter)
    QuickSingleFilterView quickSingleFilterView;
    @BindView(R2.id.rv_promo_list)
    RecyclerView rvPromoList;
    @BindView(R2.id.container_list)
    View containerList;

    @Inject
    IPromoListPresenter dPresenter;
    @Inject
    CompositeSubscription compositeSubscription;

    private RefreshHandler refreshHandler;

    private PromoMenuData promoMenuData;
    private PromoListAdapter adapter;
    private BottomSheetView bottomSheetViewInfoPromoCode;
    private boolean isLoadMore;
    private String filterSelected = "";
    private EndlessRecyclerviewListener endlessRecyclerviewListener;

    @Override
    protected void initInjector() {
        super.initInjector();
        PromoListFragmentComponent promoListComponent = DaggerPromoListFragmentComponent.builder()
                .appComponent((AppComponent) getComponent(AppComponent.class))
                .promoListFragmentModule(new PromoListFragmentModule(this))
                .build();
        promoListComponent.inject(this);
    }

    @Override
    public void renderPromoDataList(List<PromoData> promoDataList, boolean firstTimeLoad) {
        if (refreshHandler.isRefreshing()) refreshHandler.finishRefresh();
        View errorView = containerList.findViewById(com.tokopedia.core.R.id.main_retry);
        if (errorView != null) errorView.setVisibility(View.GONE);
        if (firstTimeLoad) {
            adapter.addAllItems(promoDataList);
        } else {
            adapter.addAllItemsLoadMore(promoDataList);
        }
    }

    @Override
    public void renderNextPage(boolean hasNextPage) {
        this.isLoadMore = hasNextPage;
        adapter.setHasNextPage(hasNextPage);
    }

    @Override
    public void renderErrorGetPromoDataList(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderEmptyResultGetPromoDataList() {
        handleErrorEmptyState(getString(R.string.message_error_data_empty_get_promo_list));
    }

    @Override
    public void renderErrorHttpGetPromoDataList(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorNoConnectionGetPromoDataList(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorLoadNextPage(String message, int actualPage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                dPresenter.processGetPromoListLoadMore(filterSelected, promoMenuData.getTitle());
            }
        }).showRetrySnackbar();
    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoDataListt(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void disableSwipeRefresh() {
        refreshHandler.setPullEnabled(false);
    }

    @Override
    public void enableSwipeRefresh() {
        refreshHandler.setPullEnabled(true);
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }


    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {

    }

    @Override
    public void showProgressLoading() {

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public void showToastMessage(String message) {
        if (getView() != null) Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(EXTRA_STATE_PROMO_MENU_DATA, promoMenuData);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        promoMenuData = savedState.getParcelable(EXTRA_STATE_PROMO_MENU_DATA);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.promoMenuData = arguments.getParcelable(ARG_EXTRA_PROMO_MENU_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_promo_list;
    }

    @Override
    protected void initView(View view) {
        refreshHandler = new RefreshHandler(getActivity(), view, this);
        adapter = new PromoListAdapter(new ArrayList<PromoData>(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPromoList.setLayoutManager(layoutManager);
        endlessRecyclerviewListener = new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isLoadMore) {
                    dPresenter.processGetPromoListLoadMore(filterSelected, promoMenuData.getTitle());
                }
            }
        };
        rvPromoList.addOnScrollListener(endlessRecyclerviewListener);
        rvPromoList.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        final List<QuickFilterItem> quickFilterItemList = setQuickFilterItems(promoMenuData.getPromoSubMenuDataList());
        quickSingleFilterView.renderFilter(quickFilterItemList);
        quickSingleFilterView.setDefaultItem(quickFilterItemList.get(0));
        quickSingleFilterView.setListener(new QuickSingleFilterView.ActionListener() {
            @Override
            public void selectFilter(String typeFilter) {
                String subCategoryName = getSubCategoryNameById(typeFilter);
                UnifyTracking.eventPromoListClickSubCategory(subCategoryName);
                filterSelected = typeFilter.equals(TYPE_FILTER_ALL) ?
                        promoMenuData.getAllSubCategoryId() : typeFilter;
                refreshHandler.startRefresh();
            }

            private String getSubCategoryNameById(String typeFilter) {
                for (QuickFilterItem item : quickFilterItemList) {
                    if (item.getType().equalsIgnoreCase(typeFilter)) return item.getName();
                }
                return "";
            }
        });
        quickSingleFilterView.actionSelect(0);
    }

    private List<QuickFilterItem> setQuickFilterItems(List<PromoSubMenuData> promoSubMenuDataList) {
        List<QuickFilterItem> quickFilterItemList = new ArrayList<>();
        for (int i = 0; i < promoSubMenuDataList.size(); i++) {
            QuickFilterItem quickFilterItem = new QuickFilterItem();
            quickFilterItem.setName(promoSubMenuDataList.get(i).getTitle());
            quickFilterItem.setType(promoSubMenuDataList.get(i).getId());
            quickFilterItem.setSelected(promoSubMenuDataList.get(i).isSelected());
            quickFilterItem.setColorBorder(R.color.tkpd_main_green);
            quickFilterItemList.add(quickFilterItem);
        }
        return quickFilterItemList;
    }

    @Override
    protected void setActionVar() {

    }

    public static Fragment newInstance(PromoMenuData promoMenuData) {
        Fragment fragment = new PromoListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_PROMO_MENU_DATA, promoMenuData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onItemPromoCodeCopyClipboardClicked(String promoCode, String promoName) {
        UnifyTracking.eventPromoListClickCopyToClipboardPromoCode(promoName);
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(
                "CLIP_DATA_LABEL_VOUCHER_PROMO", promoCode
        );
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        showToastMessage("Kode Voucher telah tersalin");
    }

    @Override
    public void onItemPromoClicked(PromoData promoData, int position) {
        String appLink = promoData.getAppLink();
        String redirectUrl = promoData.getPromoLink();
        if (getActivity().getApplication() instanceof TkpdCoreRouter) {
            TkpdCoreRouter tkpdCoreRouter = (TkpdCoreRouter) getActivity().getApplication();
            if (!TextUtils.isEmpty(appLink) && tkpdCoreRouter.isSupportedDelegateDeepLink(appLink))
                tkpdCoreRouter.actionApplinkFromActivity(getActivity(), appLink);
            else tkpdCoreRouter.actionOpenGeneralWebView(getActivity(), redirectUrl);
        }
        dPresenter.sendClickItemPromoListTrackingData(promoData, position, promoMenuData.getTitle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    @Override
    public void onItemPromoCodeTooltipClicked() {
        UnifyTracking.eventPromoTooltipClickOpenTooltip();
        if (bottomSheetViewInfoPromoCode == null) {
            bottomSheetViewInfoPromoCode = new BottomSheetView(getActivity());
            bottomSheetViewInfoPromoCode.renderBottomSheet(new BottomSheetView.BottomSheetField
                    .BottomSheetFieldBuilder()
                    .setTitle("Kode Promo")
                    .setBody("Masukan Kode Promo di halaman pembayaran")
                    .setImg(R.drawable.ic_promo)
                    .build());
            bottomSheetViewInfoPromoCode.setListener(new BottomSheetView.ActionListener() {
                @Override
                public void clickOnTextLink(String url) {

                }

                @Override
                public void clickOnButton(String url, String appLink) {
                    UnifyTracking.eventPromoTooltipClickCloseTooltip();
                }
            });
        }
        bottomSheetViewInfoPromoCode.show();

    }

    private void handleErrorEmptyState(String message) {
        if (refreshHandler.isRefreshing()) refreshHandler.finishRefresh();
        adapter.clearDataList();
        NetworkErrorHelper.showEmptyState(
                getActivity(), containerList, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                });
    }

    @Override
    public void onRefresh(View view) {
        endlessRecyclerviewListener.resetState();
        dPresenter.setPage(1);
        dPresenter.processGetPromoList(filterSelected, promoMenuData.getTitle());
    }
}
