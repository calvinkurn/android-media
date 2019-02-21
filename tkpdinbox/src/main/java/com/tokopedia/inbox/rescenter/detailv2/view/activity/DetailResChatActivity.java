package com.tokopedia.inbox.rescenter.detailv2.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.common.ResolutionUrl;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatActivityListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResChatActivityPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_WEBVIEW_RESO_ENABLED_TOGGLE;

/**
 * Created by yoasfs on 10/6/17.
 */

public class DetailResChatActivity
        extends BasePresenterActivity<DetailResChatActivityListener.Presenter>
        implements DetailResChatActivityListener.View, HasComponent {

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
        Intent intent = null;
        if (isToggleResoEnabled(context)) {
            intent = getWebviewIntent(context, resolutionId);
        }

        if (intent == null) {
            intent = new Intent(context, DetailResChatActivity.class);
            intent.putExtra(PARAM_RESOLUTION_ID, resolutionId);
            intent.putExtra(PARAM_SHOP_NAME, shopName);
            intent.putExtra(PARAM_IS_SELLER, false);
        }
        return intent;
    }

    public static Intent newSellerInstance(Context context, String resolutionId, String username) {
        Intent intent = null;
        if (isToggleResoEnabled(context)) {
            intent = getWebviewIntent(context, resolutionId);
        }

        if (intent == null) {
            intent = new Intent(context, DetailResChatActivity.class);
            intent.putExtra(PARAM_RESOLUTION_ID, resolutionId);
            intent.putExtra(PARAM_USER_NAME, username);
            intent.putExtra(PARAM_IS_SELLER, true);
        }
        return intent;
    }

    private static boolean isToggleResoEnabled(Context context) {
//        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
//        return remoteConfig.getBoolean(APP_WEBVIEW_RESO_ENABLED_TOGGLE);
        return true;
    }

    private static Intent getWebviewIntent(Context context, String resolutionId) {
        if (context.getApplicationContext() instanceof ResolutionRouter) {
            if (context instanceof Activity) {
                return ((ResolutionRouter)context.getApplicationContext()).getBannerWebViewIntent((Activity)context, String.format(ResolutionUrl.RESO_DETAIL, resolutionId));
            }
        }
        return null;
    }

    @DeepLink(Constants.Applinks.RESCENTER)
    public static TaskStackBuilder getCallingIntent(Context context, Bundle bundle) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Intent parentIntent;
        Intent destinationIntent = new Intent(context, DetailResChatActivity.class);
        String resoId = bundle.getString(PARAM_RESOLUTION_ID, "");
        destinationIntent.putExtra(PARAM_RESOLUTION_ID, resoId);
        String userName = MethodChecker.fromHtml(bundle.getString(PARAM_APPLINK_BUYER,"")).toString();
        String shopName = MethodChecker.fromHtml(bundle.getString(PARAM_APPLINK_SELLER,"")).toString();
        String userNameSpanned = userName.replaceAll("%20"," ");
        String shopNameSpanned = shopName.replaceAll("%20"," ");
        if (TextUtils.isEmpty(shopName)) {
            parentIntent = ResoInboxActivity.newSellerInstance(context);
            destinationIntent.putExtra(PARAM_USER_NAME, userNameSpanned);
            destinationIntent.putExtra(PARAM_IS_SELLER, true);
            bundle.putString(PARAM_USER_NAME, userNameSpanned);
        } else {
            parentIntent = ResoInboxActivity.newBuyerInstance(context);
            destinationIntent.putExtra(PARAM_SHOP_NAME, shopNameSpanned);
            destinationIntent.putExtra(PARAM_IS_SELLER,false);
            bundle.putString(PARAM_SHOP_NAME, shopNameSpanned);
        }
        destinationIntent.putExtras(bundle);
        if (context.getApplicationContext() instanceof TkpdInboxRouter){
            Intent intent = ((TkpdInboxRouter) context.getApplicationContext()).getHomeIntent(context);
            taskStackBuilder.addNextIntent(intent);
        }
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(destinationIntent);
        return taskStackBuilder;
    }

    @Override
    public void inflateFragment(Fragment fragment, String TAG, boolean isReload) {
        if (getFragmentManager().findFragmentByTag(TAG) != null && !isReload) {
            getFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core2.R.id.container,
                            getFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .add(com.tokopedia.core2.R.id.container, fragment, TAG)
                    .commit();
        }
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
        presenter = new DetailResChatActivityPresenter(this, resolutionId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_res_chat;
    }

    @Override
    protected void initView() {
        if (isSeller) {
            toolbar.setTitle(getString(R.string.complaint_from) +" "+ userName);
        } else {
            toolbar.setTitle(getString(R.string.complaint_to) +" "+ shopName);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.action_detail, 0, "");
        MenuItem menuItem = menu.findItem(R.id.action_detail); // OR THIS
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(getDetailMenuItem());
        return true;
    }

    private Drawable getDetailMenuItem() {
        TextDrawable drawable = new TextDrawable(this);
        drawable.setText(getResources().getString(R.string.detail));
        drawable.setTextColor(R.color.black_70b);
        return drawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_detail) {
            Intent intent;
            if (isSeller) {
                intent = DetailResCenterActivity.newSellerInstance(DetailResChatActivity.this, resolutionId, userName);
            } else {
                intent = DetailResCenterActivity.newBuyerInstance(DetailResChatActivity.this, resolutionId, shopName);
            }
            startActivityForResult(intent, REQUEST_GO_DETAIL);
            UnifyTracking.eventTracking(this,InboxAnalytics.eventResoChatClickDetail(resolutionId));
            return true;
        } else
            return super.onOptionsItemSelected(item);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GO_DETAIL) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.initFragment(isSeller, resolutionId, true);
            } else if (resultCode == ACTION_GO_TO_LIST) {
                finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PARAM_RESOLUTION_ID, resolutionId);
        outState.putBoolean(PARAM_IS_SELLER, isSeller);
        outState.putString(PARAM_USER_NAME, userName);
        outState.putString(PARAM_SHOP_NAME, shopName);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resolutionId = savedInstanceState.getString(PARAM_RESOLUTION_ID);
        isSeller = savedInstanceState.getBoolean(PARAM_IS_SELLER);
        if (isSeller) {
            userName = savedInstanceState.getString(PARAM_USER_NAME);
        } else {
            shopName = savedInstanceState.getString(PARAM_SHOP_NAME);
        }
        initView();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(PARAM_RESOLUTION_ID, resolutionId);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
