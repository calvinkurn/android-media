package com.tokopedia.inbox.inboxchat.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatNotifInterface;
import com.tokopedia.inbox.inboxchat.adapter.ChatPagerAdapter;
import com.tokopedia.inbox.inboxchat.fragment.InboxChatFragment;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessageold.activity.InboxMessageActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InboxChatActivity extends DrawerPresenterActivity
        implements InboxMessageConstant, NotificationReceivedListener, HasComponent, ChatNotifInterface {

    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;
    private ArrayList<Fragment> fragmentList;

//    @DeepLink(Constants.Applinks.MESSAGE)
//    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
//        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
//        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
//
//        Intent homeIntent = null;
//        if (GlobalConfig.isSellerApp()) {
//            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
//        } else {
//            homeIntent = HomeRouter.getHomeActivity(context);
//        }
//        Intent destination;
//        if(MainApplication.getInstance() instanceof RemoteConfigRouter
//                && ((RemoteConfigRouter) MainApplication.getInstance()).getBooleanConfig(TkpdInboxRouter.ENABLE_TOPCHAT)) {
//            destination = new Intent(context, InboxChatActivity.class)
//                    .setData(uri.build())
//                    .putExtras(extras);
//        } else {
//            destination = new Intent(context, InboxMessageActivity.class)
//                    .setData(uri.build())
//                    .putExtras(extras);
//        }
//
//        taskStackBuilder.addNextIntent(homeIntent);
//        taskStackBuilder.addNextIntent(destination);
//        return taskStackBuilder;
//    }

    @DeepLink(Constants.Applinks.TOPCHAT_IDLESS)
    public static Intent getCallingIntentTopchatWithoutId(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();

        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        Intent destination;
        if(remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT)) {
            destination = new Intent(context, InboxChatActivity.class)
                    .setData(uri.build())
                    .putExtras(extras);
        } else {
            destination = new Intent(context, InboxMessageActivity.class)
                    .setData(uri.build())
                    .putExtras(extras);
        }
        return destination;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());
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
    public String getScreenName() {
        return AppScreen.SCREEN_CHAT;
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
        return com.tokopedia.core.R.layout.activity_inbox_message;
    }

    @Override
    protected void initView() {
        super.initView();
//        drawer.setDrawerPosition(TkpdState.DrawerPosition.INBOX_MESSAGE);
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));

//        indicator.addTab(indicator.newTab().setText(getString(R.string.title_inbox_message_all)));
        indicator.addTab(indicator.newTab().setCustomView(R.layout.circle_text));
        ((TextView) indicator.getTabAt(indicator.getTabCount() - 1).getCustomView().findViewById(R.id.tab_title)).setText(getString(R.string.title_inbox_chat_all));
        indicator.addTab(indicator.newTab().setText(getString(R.string.title_inbox_archive)));

        if (getIntent().getExtras() != null && getIntent().getExtras().getInt(BUNDLE_POSITION, -1) != -1)
            viewPager.setCurrentItem(getIntent().getExtras().getInt(BUNDLE_POSITION, -1));
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_MESSAGE;
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

    public ChatPagerAdapter getViewPagerAdapter() {
        return new ChatPagerAdapter(getSupportFragmentManager(), initFragmentList());
    }

    public List<Fragment> initFragmentList() {
        fragmentList = new ArrayList<>();
        fragmentList.add(InboxChatFragment.createInstance(MESSAGE_ALL));
//        fragmentList.add(InboxChatFragment.createInstance(MESSAGE_ARCHIVE));
        return fragmentList;
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot() && GlobalConfig.isSellerApp()) {
            startActivity(SellerAppRouter.getSellerHomeActivity(this));
            finish();
        } else if (isTaskRoot()) {
            startActivity(HomeRouter.getHomeActivity(this));
            finish();
        }
        super.onBackPressed();

    }

    @Override
    public void onGetNotif(Bundle data) {
        super.onGetNotif(data);
        InboxChatFragment something = (InboxChatFragment) fragmentList.get(viewPager.getCurrentItem());
//        something.restackList(data);
    }

    public void showTabLayout(boolean b) {
        b = false;
        toolbar = (Toolbar) findViewById(com.tokopedia.core.R.id.app_bar);
        if (b) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            indicator.setVisibility(View.VISIBLE);
        } else {
            toolbar.setTitleTextColor(getResources().getColor(R.color.black));
            indicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccessGetTopChatNotification(int notifUnreads) {
        if (notifUnreads > 0) {
            TextView titleTextView = (TextView) toolbar.findViewById(R.id.actionbar_title);
            titleTextView.setText("Chat (" + notifUnreads + ")");
        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxChatActivity.class);
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
