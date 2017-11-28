package com.tokopedia.inbox.inboxchat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.router.RemoteConfigRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatNotifInterface;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessageold.activity.InboxMessageActivity;
import com.tokopedia.inbox.inboxmessageold.activity.InboxMessageDetailActivity;

/**
 * Created by Nisie on 5/19/16.
 */
public class ChatRoomActivity extends BasePresenterActivity
        implements InboxMessageConstant, NotificationReceivedListener, HasComponent, ChatNotifInterface {


    private static final String TAG = "INBOX_MESSAGE_DETAIL_FRAGMENT";
    public static final String PARAM_SENDER_ROLE = "PARAM_SENDER_ROLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.setCurrentActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainApplication.setCurrentActivity(null);
    }

    @Override
    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.header_chat, null);
        toolbar.addView(mCustomView);
        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("");
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setContentInsetEndWithActions(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    @DeepLink({Constants.Applinks.MESSAGE_DETAIL, Constants.Applinks.TOPCHAT})
    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        Intent detailsIntent;
        Intent parentIntent;

        if(MainApplication.getInstance() instanceof RemoteConfigRouter
                && ((RemoteConfigRouter) MainApplication.getInstance() ).getBooleanConfig(TkpdInboxRouter.ENABLE_TOPCHAT)) {
            detailsIntent = new Intent(context, ChatRoomActivity.class).putExtras(extras);
            parentIntent = new Intent(context, InboxChatActivity.class);
        } else {
            detailsIntent = new Intent(context, InboxMessageDetailActivity.class).putExtras
                    (extras);
            parentIntent = new Intent(context, InboxMessageActivity.class);
        }

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_MESSAGE_DETAIL_VIEW;
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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, ChatRoomFragment.createInstance(getIntent().getExtras()),
                        TAG)
                .commit();
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
    protected boolean isLightToolbarThemes() {
        return true;
    }


    @Override
    public void onGetNotif() {

    }

    @Override
    public void onRefreshCart(int status) {

    }

    @Override
    public void onGetNotif(Bundle data) {
        ChatRoomFragment something = (ChatRoomFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        something.restackList(data);
    }

    public static Intent getCallingIntent(Context context, String nav, String messageId,
                                          int position, String senderName, String senderTag,
                                          String senderId, String role, int mode, String keyword, String image) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NAV, nav);
        bundle.putParcelable(PARAM_MESSAGE, null);
        bundle.putString(PARAM_MESSAGE_ID, messageId);
        bundle.putInt(PARAM_POSITION, position);
        bundle.putString(PARAM_SENDER_NAME, senderName);
        bundle.putString(PARAM_SENDER_IMAGE, image);
        bundle.putString(PARAM_SENDER_TAG, senderTag);
        bundle.putString(PARAM_SENDER_ID, senderId);
        bundle.putString(PARAM_SENDER_ROLE, role);
        bundle.putInt(PARAM_MODE, mode);
        bundle.putString(PARAM_KEYWORD, keyword);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
