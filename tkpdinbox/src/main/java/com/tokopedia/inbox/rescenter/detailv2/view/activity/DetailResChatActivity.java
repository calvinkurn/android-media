package com.tokopedia.inbox.rescenter.detailv2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.common.ResolutionUrl;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatActivityListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;

/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatActivity extends BasePresenterActivity<DetailResChatActivityListener.Presenter> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";
    public static final String PARAM_SHOP_NAME = "shopName";
    public static final String PARAM_USER_NAME = "buyerName";
    public static final String PARAM_IS_SELLER = "is_seller";

    public static final String PARAM_APPLINK_SELLER = "shopName";
    public static final String PARAM_APPLINK_BUYER = "buyerName";

    public static final int REQUEST_GO_DETAIL = 8888;
    public static final int ACTION_GO_TO_LIST = 6123;
    private String resolutionId;
    private String shopName;
    private String userName;
    private boolean isSeller;

    public static Intent newBuyerInstance(Context context, String resolutionId, String shopName) {
        return getApplinkIntent(context, resolutionId);
    }

    private static Intent getApplinkIntent(Context context, String resolutionId) {
        if (context.getApplicationContext() instanceof ResolutionRouter) {
            if (GlobalConfig.isSellerApp()) {
                return ((ResolutionRouter) context.getApplicationContext()).getSellerWebViewIntent(context,
                        String.format(ResolutionUrl.RESO_DETAIL, resolutionId));
            } else {
                return ((ResolutionRouter) context.getApplicationContext()).getApplinkIntent(context,
                        String.format(ResolutionUrl.RESO_APPLINK + ResolutionUrl.HOSTNAME + ResolutionUrl.RESO_DETAIL, resolutionId));
            }
        }
        return null;
    }

    private static Intent getApplinkIntentCenter(Context context, String resolutionId) {
        if (context.getApplicationContext() instanceof ResolutionRouter) {
            return ((ResolutionRouter) context.getApplicationContext()).getApplinkIntent(context,
                    String.format(ResolutionUrl.RESO_APPLINK + ResolutionUrl.HOSTNAME + ResolutionUrl.RESO_DETAIL_NEW, resolutionId));
        }
        return null;
    }

    @DeepLink(Constants.Applinks.RESCENTER)
    public static TaskStackBuilder getCallingIntent(Context context, Bundle bundle) {
        return taskStackBuilderGenerator(context, bundle, Constants.Applinks.RESCENTER);
    }


    @DeepLink(Constants.Applinks.RESCENTER_CENTER)
    public static TaskStackBuilder getResCenterCallingIntent(Context context, Bundle bundle) {
        return taskStackBuilderGenerator(context, bundle, Constants.Applinks.RESCENTER_CENTER);
    }

    private static TaskStackBuilder taskStackBuilderGenerator(Context context, Bundle bundle, String rescenter_type) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Intent parentIntent;
        Intent destinationIntent = null;
        String resoId = bundle.getString(PARAM_RESOLUTION_ID, "");

        if (rescenter_type.equals(Constants.Applinks.RESCENTER)) {
            destinationIntent = getApplinkIntent(context, resoId);
        } else {
            destinationIntent = getApplinkIntentCenter(context, resoId);
        }

        destinationIntent.putExtra(PARAM_RESOLUTION_ID, resoId);
        String userName = MethodChecker.fromHtml(bundle.getString(PARAM_APPLINK_BUYER, "")).toString();
        String shopName = MethodChecker.fromHtml(bundle.getString(PARAM_APPLINK_SELLER, "")).toString();
        String userNameSpanned = userName.replaceAll("%20", " ");
        String shopNameSpanned = shopName.replaceAll("%20", " ");
        if (TextUtils.isEmpty(shopName)) {
            parentIntent = ResoInboxActivity.newSellerInstance(context);
            destinationIntent.putExtra(PARAM_USER_NAME, userNameSpanned);
            destinationIntent.putExtra(PARAM_IS_SELLER, true);
            bundle.putString(PARAM_USER_NAME, userNameSpanned);
        } else {
            parentIntent = ResoInboxActivity.newBuyerInstance(context);
            destinationIntent.putExtra(PARAM_SHOP_NAME, shopNameSpanned);
            destinationIntent.putExtra(PARAM_IS_SELLER, false);
            bundle.putString(PARAM_SHOP_NAME, shopNameSpanned);
        }
        destinationIntent.putExtras(bundle);
        if (context.getApplicationContext() instanceof TkpdInboxRouter) {
            Intent intent = ((TkpdInboxRouter) context.getApplicationContext()).getHomeIntent(context);
            taskStackBuilder.addNextIntent(intent);
        }
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(destinationIntent);
        return taskStackBuilder;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        resolutionId = extras.getString(PARAM_RESOLUTION_ID);
        isSeller = extras.getBoolean(PARAM_IS_SELLER);
        if (isSeller) {
            userName = MethodChecker.fromHtml(extras.getString(PARAM_USER_NAME)).toString();
        } else {
            shopName = MethodChecker.fromHtml(extras.getString(PARAM_SHOP_NAME)).toString();
        }
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
        if (isSeller) {
            toolbar.setTitle(getString(R.string.complaint_from) + " " + userName);
        } else {
            toolbar.setTitle(getString(R.string.complaint_to) + " " + shopName);
        }
        presenter.initFragment(isSeller, resolutionId, false);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setPadding(0, 0, 30, 0);
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
