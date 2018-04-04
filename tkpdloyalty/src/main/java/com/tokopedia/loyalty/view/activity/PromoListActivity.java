package com.tokopedia.loyalty.view.activity;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.widget.TouchViewPager;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.R2;
import com.tokopedia.loyalty.applink.LoyaltyAppLink;
import com.tokopedia.loyalty.di.component.DaggerPromoListActivityComponent;
import com.tokopedia.loyalty.di.component.PromoListActivityComponent;
import com.tokopedia.loyalty.di.module.PromoListActivityModule;
import com.tokopedia.loyalty.view.adapter.PromoPagerAdapter;
import com.tokopedia.loyalty.view.compoundview.MenuPromoTab;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.presenter.IPromoListActivityPresenter;
import com.tokopedia.loyalty.view.view.IPromoListActivityView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoListActivity extends BasePresenterActivity implements HasComponent<AppComponent>, IPromoListActivityView {

    @BindView(R2.id.view_pager)
    TouchViewPager viewPager;
    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R2.id.container_error)
    View containerError;

    private PromoPagerAdapter adapter;

    @Inject
    IPromoListActivityPresenter dPresenter;

    @DeepLink(LoyaltyAppLink.PROMO_NATIVE)
    public static Intent appLinkInstance(Context context) {
        return new Intent(context, PromoListActivity.class);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, PromoListActivity.class);
    }

    @SuppressWarnings("unused")
    @DeepLink(Constants.Applinks.PROMO_LIST)
    public static Intent getAppLinkIntent(Context context, Bundle extras) {
        return PromoListActivity.newInstance(context);
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        PromoListActivityComponent promoListActivityComponent = DaggerPromoListActivityComponent.builder()
                .appComponent(getComponent())
                .promoListActivityModule(new PromoListActivityModule(this))
                .build();
        promoListActivityComponent.inject(this);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_promo_list;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {
        if (isLightToolbarThemes())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setElevation(0);
                toolbar.setBackgroundResource(com.tokopedia.core.R.color.white);
            } else {
                toolbar.setBackgroundColor(getResources().getColor(R.color.white));
            }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        dPresenter.processGetPromoMenu();
    }

    @Override
    public void renderPromoMenuDataList(final List<PromoMenuData> promoMenuDataList) {
        viewPager.setOffscreenPageLimit(promoMenuDataList.size());
        adapter = new PromoPagerAdapter(getFragmentManager(), promoMenuDataList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < promoMenuDataList.size(); i++) {
            MenuPromoTab menuPromoTab = new MenuPromoTab(this);
            menuPromoTab.renderData(promoMenuDataList.get(i));
            tabLayout.getTabAt(i).setCustomView(menuPromoTab);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    ((MenuPromoTab) tab.getCustomView()).renderActiveState();
                }
                UnifyTracking.eventPromoListClickCategory(
                        promoMenuDataList.get(tab.getPosition()).getTitle()
                );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView() != null) {
                    ((MenuPromoTab) tab.getCustomView()).renderNormalState();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        TabLayout.Tab firstTab = tabLayout.getTabAt(0);
        if (firstTab != null) {
            firstTab.select();
            ((MenuPromoTab) firstTab.getCustomView()).renderActiveState();
            UnifyTracking.eventPromoListClickCategory(
                    promoMenuDataList.get(firstTab.getPosition()).getTitle()
            );
        }
    }

    @Override
    public void renderErrorGetPromoMenuDataList(String message) {
        handlerError(message);
    }

    @Override
    public void renderErrorHttpGetPromoMenuDataList(String message) {
        handlerError(message);
    }

    @Override
    public void renderErrorNoConnectionGetPromoMenuDataList(String message) {
        handlerError(message);
    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoMenuDataListt(String message) {
        handlerError(message);
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
    protected boolean isLightToolbarThemes() {
        return true;
    }


    private void handlerError(String message) {
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        containerError.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(this, containerError,
                message, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        dPresenter.processGetPromoMenu();
                        containerError.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);
                    }
                });
    }
}
