package com.tokopedia.inbox.rescenter.inboxv2.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.common.ResolutionUrl;
import com.tokopedia.inbox.common.applink.ApplinkConstant;
import com.tokopedia.inbox.rescenter.inboxv2.view.fragment.ResoInboxFragment;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_WEBVIEW_RESO_ENABLED_TOGGLE;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxActivity extends BasePresenterActivity implements HasComponent {
    public static final String TAG = ResoInboxFragment.class.getSimpleName();
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
        Intent intent = null;
        if (isToggleResoEnabled(context)) {
            intent = getWebviewIntent(context, ResolutionUrl.RESO_INBOX_SELLER);
        }

        if (intent == null) {
            intent = new Intent(context, ResoInboxActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(PARAM_IS_SELLER, true);
            bundle.putString(PARAM_HEADER_TEXT, "Komplain Sebagai Penjual");
            intent.putExtras(bundle);
        }
        return intent;
    }

    public static Intent newBuyerInstance(Context context) {
        Intent intent = null;
        if (isToggleResoEnabled(context)) {
            intent = getWebviewIntent(context, ResolutionUrl.RESO_INBOX_BUYER);
        }

        if (intent == null) {
            intent = new Intent(context, ResoInboxActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(PARAM_IS_SELLER, false);
            bundle.putString(PARAM_HEADER_TEXT, "Komplain Sebagai Pembeli");
            intent.putExtras(bundle);
        }
        return intent;
    }

    private static boolean isToggleResoEnabled(Context context) {
//        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
//        return remoteConfig.getBoolean(APP_WEBVIEW_RESO_ENABLED_TOGGLE);
        return true;
    }

    private static Intent getWebviewIntent(Context context, String url) {
        if (context.getApplicationContext() instanceof ResolutionRouter) {
            if (context instanceof Activity) {
                return ((ResolutionRouter)context.getApplicationContext()).getBannerWebViewIntent((Activity)context, url);
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
        Fragment fragment = ResoInboxFragment.getFragmentInstance(getIntent().getExtras());
        if (getSupportFragmentManager().findFragmentByTag(TAG) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core2.R.id.container,
                            getSupportFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(com.tokopedia.core2.R.id.container, fragment, TAG)
                    .commit();
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

    }

    protected Fragment getNewFragment() {
        return ResoInboxFragment.getFragmentInstance(getIntent().getExtras());
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
