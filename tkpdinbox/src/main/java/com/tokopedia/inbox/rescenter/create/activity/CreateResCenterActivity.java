package com.tokopedia.inbox.rescenter.create.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.common.ResolutionUrl;
import com.tokopedia.inbox.rescenter.create.presenter.CreateResCenterPresenter;

public class CreateResCenterActivity extends BasePresenterActivity<CreateResCenterPresenter> {

    public static Intent getCreateResCenterActivityIntent(Context context, String orderId) {
        return getApplinkIntent(context, orderId);
    }

    public static Intent newInstance(Context context, String orderID) {
        return getApplinkIntent(context, orderID);
    }

    public static Intent newRecomplaintInstance(Context context, String orderID, String resolutionId) {
        return getApplinkIntent(context, orderID);
    }

    private static Intent getApplinkIntent(Context context, String orderId) {
        if (context.getApplicationContext() instanceof ResolutionRouter) {
            if (GlobalConfig.isSellerApp()) {
                return ((ResolutionRouter) context.getApplicationContext()).getSellerWebViewIntent(context,
                        String.format(ResolutionUrl.RESO_CREATE, orderId));
            } else {
                return ((ResolutionRouter) context.getApplicationContext()).getApplinkIntent(context,
                        String.format(ResolutionUrl.RESO_APPLINK + ResolutionUrl.HOSTNAME + ResolutionUrl.RESO_CREATE, orderId));
            }
        }
        return null;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_ADD;
    }

    @Override
    protected void setupURIPass(Uri data) {
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_resolution_center;
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

    }

    @Override
    public void onBackPressed() {
    }
}
