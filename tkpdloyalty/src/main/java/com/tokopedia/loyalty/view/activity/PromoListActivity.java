package com.tokopedia.loyalty.view.activity;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.widget.TouchViewPager;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.design.quickfilter.QuickFilterView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.R2;
import com.tokopedia.loyalty.di.component.DaggerPromoListActivityComponent;
import com.tokopedia.loyalty.di.component.PromoListActivityComponent;
import com.tokopedia.loyalty.di.module.PromoListActivityModule;
import com.tokopedia.loyalty.view.adapter.PromoPagerAdapter;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.presenter.IPromoListActivityPresenter;
import com.tokopedia.loyalty.view.view.IPromoListActivityView;

import java.util.ArrayList;
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

    private PromoPagerAdapter adapter;

    @Inject
    IPromoListActivityPresenter dPresenter;

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
    protected int getLayoutId() {
        return R.layout.activity_promo_list;
    }

    @Override
    protected void initView() {

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
    public void renderPromoMenuDataList(List<PromoMenuData> promoMenuDataList) {
        for (PromoMenuData promoMenuData : promoMenuDataList) {
            Toast.makeText(this, promoMenuData.getTitle(), Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < promoMenuDataList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(promoMenuDataList.get(i).getTitle()).setIcon(R.drawable.ic_action_send));
        }
        viewPager.setOffscreenPageLimit(promoMenuDataList.size());
        adapter = new PromoPagerAdapter(getFragmentManager(), promoMenuDataList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.setIcon(R.drawable.ic_action_send);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(R.drawable.ic_wishlist);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void renderErrorGetPromoMenuDataList(String message) {

    }

    @Override
    public void renderErrorHttpGetPromoMenuDataList(String message) {

    }

    @Override
    public void renderErrorNoConnectionGetPromoMenuDataList(String message) {

    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoMenuDataListt(String message) {

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
}
