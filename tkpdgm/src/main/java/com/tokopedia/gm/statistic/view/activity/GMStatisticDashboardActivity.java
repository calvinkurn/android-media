package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticDashboardFragment;
import com.tokopedia.seller.product.draft.view.fragment.ProductDraftListFragment;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GMStatisticDashboardActivity extends DrawerPresenterActivity
        implements SessionHandler.onLogoutListener, HasComponent<GoldMerchantComponent> {

    public static final String TAG = GMStatisticDashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new GMStatisticDashboardFragment(), TAG).commit();
        }
    }

    @Override
    public void onErrorGetDeposit(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetNotificationDrawer(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetProfile(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetTokoCash(String errorMessage) {
        // no op
    }

    @Override
    public void onErrorGetTopPoints(String errorMessage) {
        // no op
    }

    @Override
    public void onServerError() {
        // no op
    }

    @Override
    public void onTimezoneError() {
        // no op
    }

    @Override
    public void onHadesTimeout() {
        // no op
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_GM_STAT;
    }

    @Override
    protected void setupURIPass(Uri data) {
        // no op
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        // no op
    }

    @Override
    protected void initialPresenter() {
        // no op
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void setViewListener() {
        // no op
    }

    @Override
    protected void initVar() {
        // no op
    }

    @Override
    protected void setActionVar() {
        // no op
    }

    @Override
    public String getScreenName() {
        return AppScreen.STATISTIC_PAGE;
    }

    @Override
    public GoldMerchantComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getGoldMerchantComponent();
    }
}
