package com.tokopedia.loyalty.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickFilterView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.R2;
import com.tokopedia.loyalty.di.component.DaggerPromoListFragmentComponent;
import com.tokopedia.loyalty.di.component.PromoListFragmentComponent;
import com.tokopedia.loyalty.di.module.PromoListFragmentModule;
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

public class PromoListFragment extends BasePresenterFragment implements IPromoListView {
    private static final String ARG_EXTRA_PROMO_MENU_DATA = "ARG_EXTRA_PROMO_MENU_DATA";

    @BindView(R2.id.quick_filter)
    LinearLayout filterLayout;

    @Inject
    IPromoListPresenter dPresenter;
    @Inject
    CompositeSubscription compositeSubscription;

    private PromoMenuData promoMenuData;
    private QuickFilterView quickFilterView;

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
    public void renderPromoDataList(List<PromoData> couponData) {
        Toast.makeText(context, couponData.get(0).getPeriodFormatted(), Toast.LENGTH_SHORT).show();
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

            }

            @Override
            public void selectFilter() {

            }
        });
        filterLayout.addView(quickFilterView);

        dPresenter.processGetPromoList(promoMenuData.getAllSubCategoryId());
    }

    private List<QuickFilterItem> setQuickFilterItems(List<PromoSubMenuData> promoSubMenuDataList) {
        List<QuickFilterItem> quickFilterItemList = new ArrayList<>();
        for (int i = 0; i < promoSubMenuDataList.size(); i++) {
            QuickFilterItem quickFilterItem = new QuickFilterItem();
            quickFilterItem.setName(promoSubMenuDataList.get(i).getTitle());
            quickFilterItem.setType(promoSubMenuDataList.get(i).getId());
            quickFilterItem.setSelected(promoSubMenuDataList.get(i).isSelected());
            quickFilterItem.setColorBorder(R.color.grey_hint);
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
}
