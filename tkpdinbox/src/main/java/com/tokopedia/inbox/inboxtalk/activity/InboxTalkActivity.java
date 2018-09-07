package com.tokopedia.inbox.inboxtalk.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.inbox.inboxtalk.fragment.InboxTalkFragment;
import com.tokopedia.core.talk.receiver.intentservice.InboxTalkIntentService;
import com.tokopedia.core.talk.receiver.intentservice.InboxTalkResultReceiver;
import com.tokopedia.inbox.inboxtalk.listener.InboxTalkActivityView;
import com.tokopedia.inbox.inboxtalk.presenter.InboxTalkActivityPresenterImpl;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InboxTalkActivity extends DrawerPresenterActivity implements
        InboxTalkActivityView,
        NotificationReceivedListener, InboxTalkResultReceiver.Receiver {

    private static final String BUNDLE_POSITION = "INBOX_TALK_POSITION";

    private static final String MY_PRODUCT = "inbox-talk-my-product";
    private static final String INBOX_ALL = "inbox-talk";
    private static final String FOLLOWING = "inbox-talk-following";
    PagerAdapter adapter;

    @BindView(R2.id.pager)
    ViewPager mViewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;


    private Boolean ContextualStats = false;
    private ActionMode mode;
    private Boolean isLogin;
    private String[] contentArray;

    private Boolean fromNotif = false;
    private Boolean forceUnread;
    InboxTalkResultReceiver mReceiver;

    @DeepLink(Constants.Applinks.TALK)
    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }

        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent destination = new Intent(context, InboxTalkActivity.class)
                .setData(uri.build())
                .putExtras(extras);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(destination);
        return taskStackBuilder;
    }

    @Override
    public void onStart() {
        super.onStart();
        isLogin = SessionHandler.isV4Login(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        initResultReceiver();

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_TALK;
    }

    private void setContent() {
        if (GlobalConfig.isSellerApp()) {
            setContentSellerApp();
        } else {
            setContentBuyerApp();
        }

        for (String content : contentArray) {
            indicator.addTab(indicator.newTab().setText(content));
        }
    }

    private void setContentSellerApp() {
        contentArray = new String[]{getString(R.string.title_my_product)};
        indicator.setVisibility(View.GONE);
    }

    private void setContentBuyerApp() {
        if (checkHasNoShop()) {
            contentArray = new String[]{getString(R.string.title_menu_all)};
            indicator.setVisibility(View.GONE);
        } else {
            contentArray = new String[]{getString(R.string.title_menu_all),
                    getString(R.string.title_my_product),
                    getString(R.string.title_following)};
            indicator.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkHasNoShop() {
        return !SessionHandler.isUserHasShop(this);
    }

    private void getExtras() {
        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());
    }

    private void initResultReceiver() {
        mReceiver = new InboxTalkResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        if (isLogin != SessionHandler.isV4Login(getBaseContext())) {
            finish();
        }
        MainApplication.setCurrentActivity(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MainApplication.setCurrentActivity(null);
        super.onPause();
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxTalkActivityPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inbox_talk;
    }

    @Override
    protected void initView() {
        super.initView();
//        drawer.setDrawerPosition(TkpdState.DrawerPosition.INBOX_TALK);
        ButterKnife.bind(this);
        setContent();
        adapter = new PagerAdapter(getFragmentManager(), getFragmentList());
        mViewPager.setOffscreenPageLimit(contentArray.length);
        mViewPager.setAdapter(adapter);

        if (getIntent().getExtras() != null && getIntent().getExtras().getInt(BUNDLE_POSITION, -1) != -1) {
            mViewPager.setCurrentItem(getIntent().getExtras().getInt(BUNDLE_POSITION));
        }
    }

    private List<Fragment> getFragmentList() {
        forceUnread = getIntent().getBooleanExtra(Constants.EXTRA_UNREAD, false);
        List<Fragment> fragmentList = new ArrayList<>();
        if (GlobalConfig.isSellerApp()) {
            fragmentList.add(InboxTalkFragment.createInstance(MY_PRODUCT, forceUnread));
        } else {
            if (checkHasNoShop()) {
                fragmentList.add(InboxTalkFragment.createInstance(INBOX_ALL, forceUnread));
            } else {
                fragmentList.add(InboxTalkFragment.createInstance(INBOX_ALL, forceUnread));
                fragmentList.add(InboxTalkFragment.createInstance(MY_PRODUCT, forceUnread));
                fragmentList.add(InboxTalkFragment.createInstance(FOLLOWING, forceUnread));
            }
        }
        return fragmentList;
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_TALK;
    }

    @Override
    protected void setViewListener() {
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(mViewPager));
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onGetNotif() {
//        if (MainApplication.getNotificationStatus()) {
//            drawer.getNotification();
//        }
//        if (MainApplication.getDrawerStatus()) {
//            drawer.updateData();
//        }
    }

    @Override
    public void onRefreshCart(int status) {
        LocalCacheHandler Cache = new LocalCacheHandler(getBaseContext(), DrawerHelper.DRAWER_CACHE);
        Cache.putInt(DrawerNotification.IS_HAS_CART, status);
        Cache.applyEditor();
        invalidateOptionsMenu();
        MainApplication.resetCartStatus(false);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        InboxTalkFragment fragment = (InboxTalkFragment) adapter.getItem(mViewPager.getCurrentItem());

        if (fragment != null && fragment.getActivity() != null) {
            switch (resultCode) {
                case InboxTalkIntentService.STATUS_SUCCESS_FOLLOW:
                case InboxTalkIntentService.STATUS_SUCCESS_DELETE:
                case InboxTalkIntentService.STATUS_SUCCESS_REPORT:
                    onReceiveResultSuccess(fragment, resultData, resultCode);
                    break;
                case InboxTalkIntentService.STATUS_ERROR_FOLLOW:
                case InboxTalkIntentService.STATUS_ERROR_DELETE:
                case InboxTalkIntentService.STATUS_ERROR_REPORT:
                    onReceiveResultError(fragment, resultData, resultCode);
                    break;
            }
        } else {
            Intent intent = getIntent();
            intent.putExtra(BUNDLE_POSITION, mViewPager.getCurrentItem());
            finish();
            startActivity(intent);
        }
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData, int resultCode) {
        ((InboxTalkFragment) fragment).onErrorAction(resultData, resultCode);
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData, int resultCode) {
        ((InboxTalkFragment) fragment).onSuccessAction(resultData, resultCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        drawer.getNotification();
    }

    public void followTalk(Bundle param) {
        InboxTalkIntentService.startAction(this, param, mReceiver,
                InboxTalkIntentService.ACTION_FOLLOW);
    }

    public void deleteTalk(Bundle param) {
        InboxTalkIntentService.startAction(this, param, mReceiver,
                InboxTalkIntentService.ACTION_DELETE);
    }

    public void reportTalk(Bundle param) {
        InboxTalkIntentService.startAction(this, param, mReceiver,
                InboxTalkIntentService.ACTION_REPORT);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();

        public PagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
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

}
