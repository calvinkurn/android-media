package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticDashboardFragment;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GMStatisticDashboardActivity extends DrawerPresenterActivity
        implements SessionHandler.onLogoutListener, HasComponent<GoldMerchantComponent> {

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_GM_STAT;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.content_gmstat;
    }

    @Override
    protected void initViews() {
        super.initViews();

        if (!isAfterRotate) {
            inflateNewFragment(new GMStatisticDashboardFragment());
        }
    }

    private void inflateNewFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_gmstat_fragment_container, fragment, GMStatisticDashboardFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //[START] unused methods
    @Override
    protected void setupURIPass(Uri data) {
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initVar() {
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public String getScreenName() {
        return AppScreen.STATISTIC_PAGE;
    }

    @Override
    public GoldMerchantComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getGoldMerchantComponent(getActivityModule());
    }
}
