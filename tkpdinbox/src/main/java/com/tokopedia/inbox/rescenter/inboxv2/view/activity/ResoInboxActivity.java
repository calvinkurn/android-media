package com.tokopedia.inbox.rescenter.inboxv2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.common.ResolutionUrl;
import com.tokopedia.inbox.common.applink.ApplinkConstant;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxActivity extends BasePresenterActivity implements HasComponent {
    public static final String PARAM_IS_SELLER = "is_seller";
    public static final String PARAM_HEADER_TEXT = "header_text";

    @DeepLink(ApplinkConstant.RESCENTER_BUYER)
    public static Intent newApplinkBuyerInstance(Context context, Bundle bundle) {
        return newBuyerInstance(context).putExtras(bundle);
    }

    @DeepLink(ApplinkConstant.RESCENTER_SELLER)
    public static Intent newApplinkSellerInstance(Context context, Bundle bundle) {
        return newSellerInstance(context).putExtras(bundle);
    }

    public static Intent newSellerInstance(Context context) {
        return getApplinkIntent(context, ResolutionUrl.HOSTNAME + ResolutionUrl.RESO_INBOX_SELLER);
    }

    public static Intent newBuyerInstance(Context context) {
        return getApplinkIntent(context, ResolutionUrl.HOSTNAME + ResolutionUrl.RESO_INBOX_BUYER);
    }

    private static Intent getApplinkIntent(Context context, String url) {
        if (context.getApplicationContext() instanceof ResolutionRouter) {
            if (GlobalConfig.isSellerApp()) {
                return ((ResolutionRouter) context.getApplicationContext()).getSellerWebViewIntent(context,
                        url);
            } else {
                return ((ResolutionRouter) context.getApplicationContext()).getApplinkIntent(context,
                        ResolutionUrl.RESO_APPLINK + url);
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle(getIntent().getStringExtra(PARAM_HEADER_TEXT));
    }

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
    protected int getLayoutId() {
        return R.layout.activity_detail_res_chat;
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
    public Object getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
