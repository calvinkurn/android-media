package com.tokopedia.seller.product.manage.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.base.presentation.BaseTemporaryDrawerActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageFragment;
import com.tokopedia.seller.product.manage.view.fragment.ProductManageSellerFragment;

/**
 * Created by zulfikarrahman on 9/25/17.
 */
@DeepLink(ApplinkConst.PRODUCT_MANAGE)
public class ProductManageActivity extends BaseTemporaryDrawerActivity implements HasComponent<ProductComponent> {

    public static final String TAG = ProductManageActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ProductManageSellerFragment(), TAG).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLogin();
    }

    private void checkLogin() {
        if (getApplication() instanceof TkpdCoreRouter) {
            if (!SessionHandler.isV4Login(this)) {
                startActivity(((TkpdCoreRouter) getApplication()).getLoginIntent(this));
                finish();
            } else if (!SessionHandler.isUserHasShop(this)) {
                startActivity(((TkpdCoreRouter) getApplication()).getHomeIntent(this));
                finish();
            }
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
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.MANAGE_PRODUCT;
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
        return AppScreen.SCREEN_MANAGE_PROD;
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }
}
