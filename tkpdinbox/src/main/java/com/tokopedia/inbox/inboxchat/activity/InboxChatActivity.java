package com.tokopedia.inbox.inboxchat.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.inboxchat.adapter.ChatPagerAdapter;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxchat.fragment.InboxChatFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InboxChatActivity extends DrawerPresenterActivity
        implements InboxMessageConstant{

    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;

    @DeepLink(Constants.Applinks.MESSAGE)
    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();

        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        Intent destination = new Intent(context, InboxChatActivity.class)
                .setData(uri.build())
                .putExtras(extras);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_MESSAGE;
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
        indicator.addTab(indicator.newTab().setCustomView(com.tokopedia.core.R.layout.circle_text));
        ((TextView)indicator.getTabAt(indicator.getTabCount()-1).getCustomView().findViewById(com.tokopedia.core.R.id.tab_title)).setText("PERCAKAPAN");
//        indicator.addTab(indicator.newTab().setText(getString(R.string.title_inbox_sent)));
        indicator.addTab(indicator.newTab().setText(getString(com.tokopedia.core.R.string.title_inbox_archive)));
        indicator.addTab(indicator.newTab().setText(getString(com.tokopedia.core.R.string.title_inbox_trash)));

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
        return new ChatPagerAdapter(getSupportFragmentManager(), getFragmentList());
    }

    public List<android.support.v4.app.Fragment> getFragmentList() {
        List<android.support.v4.app.Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(InboxChatFragment.createInstance(MESSAGE_ALL));
//        fragmentList.add(InboxChatFragment.createInstance(MESSAGE_SENT));
        fragmentList.add(InboxChatFragment.createInstance(MESSAGE_ARCHIVE));
        fragmentList.add(InboxChatFragment.createInstance(MESSAGE_TRASH));
        return fragmentList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getFragmentManager().findFragmentById(com.tokopedia.core.R.id.pager).onActivityResult(requestCode,
                resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot() && GlobalConfig.isSellerApp()) {
            startActivity(SellerAppRouter.getSellerHomeActivity(this));
            finish();
        } else if (isTaskRoot()){
            startActivity(HomeRouter.getHomeActivity(this));
            finish();
        }
        super.onBackPressed();

    }
}