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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickFilterView;
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

public class PromoListFragment extends BasePresenterFragment implements IPromoListView, PromoListAdapter.ActionListener {
    private static final String ARG_EXTRA_PROMO_MENU_DATA = "ARG_EXTRA_PROMO_MENU_DATA";

    @BindView(R2.id.quick_filter)
    LinearLayout filterLayout;
    @BindView(R2.id.rv_promo_list)
    RecyclerView rvPromoList;

    @Inject
    IPromoListPresenter dPresenter;
    @Inject
    CompositeSubscription compositeSubscription;

    private PromoMenuData promoMenuData;
    private QuickFilterView quickFilterView;
    private PromoListAdapter adapter;
    private BottomSheetView bottomSheetViewInfoPromoCode;

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
    public void renderPromoDataList(List<PromoData> promoDataList) {
        adapter.addAllItems(promoDataList);
    }

    @Override
    public void renderErrorGetPromoDataList(String message) {

    }

    @Override
    public void renderErrorHttpGetPromoDataList(String message) {

    }

    @Override
    public void renderErrorNoConnectionGetPromoDataList(String message) {

    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoDataListt(String message) {

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

    }

    @Override
    public void onRestoreState(Bundle savedState) {

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
        quickFilterView = new QuickFilterView(getActivity());
        adapter = new PromoListAdapter(new ArrayList<PromoData>(), this);
        rvPromoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPromoList.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        clearHolderView(filterLayout);
        quickFilterView.renderFilter(setQuickFilterItems(promoMenuData.getPromoSubMenuDataList()));
        quickFilterView.setListener(new QuickFilterView.ActionListener() {
            @Override
            public void clearFilter() {
                for (int i = 0; i < promoMenuData.getPromoSubMenuDataList().size(); i++) {
                    if (promoMenuData.getPromoSubMenuDataList().get(i).isSelected()) {
                        promoMenuData.getPromoSubMenuDataList().get(i).setSelected(false);
                    }
                }
                dPresenter.processGetPromoList(promoMenuData.getAllSubCategoryId());
            }

            @Override
            public void selectFilter(String typeFilter) {
                for (int i = 0; i < promoMenuData.getPromoSubMenuDataList().size(); i++) {
                    if (typeFilter.equals(promoMenuData.getPromoSubMenuDataList().get(i).getId())) {
                        promoMenuData.getPromoSubMenuDataList().get(i).setSelected(true);
                    } else {
                        promoMenuData.getPromoSubMenuDataList().get(i).setSelected(false);
                    }
                }
                dPresenter.processGetPromoList(typeFilter);
            }
        });
        filterLayout.addView(quickFilterView);
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

    private void clearHolderView(LinearLayout holderView) {
        if (holderView.getChildCount() > 0) {
            holderView.removeAllViews();
        }
    }

    @Override
    public void onItemPromoCodeCopyClipboardClicked(String promoCode) {
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
    public void onItemPromoClicked(PromoData promoData) {
        String appLink = promoData.getAppLink();
        String redirectUrl = promoData.getPromoLink();
        if (getActivity().getApplication() instanceof TkpdCoreRouter) {
            TkpdCoreRouter tkpdCoreRouter = (TkpdCoreRouter) getActivity().getApplication();
            if (!TextUtils.isEmpty(appLink) && tkpdCoreRouter.isSupportedDelegateDeepLink(appLink))
                tkpdCoreRouter.actionAppLink(getActivity(), appLink);
            else tkpdCoreRouter.actionOpenGeneralWebView(getActivity(), redirectUrl);
        }
    }

    @Override
    public void onItemPromoCodeTooltipClicked() {
        if (bottomSheetViewInfoPromoCode == null) {
            bottomSheetViewInfoPromoCode = new BottomSheetView(getActivity());
            bottomSheetViewInfoPromoCode.renderBottomSheet(new BottomSheetView.BottomSheetField
                    .BottomSheetFieldBuilder()
                    .setTitle("Kode Promo")
                    .setBody("Masukan Kode Promo di halaman pembayaran")
                    .setImg(R.drawable.ic_promo)
                    .build());
        }
        bottomSheetViewInfoPromoCode.show();

    }
}
