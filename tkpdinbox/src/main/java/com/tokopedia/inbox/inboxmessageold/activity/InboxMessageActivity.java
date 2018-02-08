package com.tokopedia.inbox.inboxmessageold.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.inbox.inboxmessageold.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessageold.adapter.MessagePagerAdapter;
import com.tokopedia.inbox.inboxmessageold.fragment.InboxMessageFragment;
import com.tokopedia.inbox.inboxmessageold.intentservice.InboxMessageIntentService;
import com.tokopedia.inbox.inboxmessageold.intentservice.InboxMessageResultReceiver;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Nisie on 5/9/16.
 */
public class InboxMessageActivity extends DrawerPresenterActivity
        implements InboxMessageFragment.DoActionInboxMessageListener,
        InboxMessageConstant, InboxMessageResultReceiver.Receiver {

    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;

    InboxMessageResultReceiver mReceiver;

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
        Intent destination = new Intent(context, InboxMessageActivity.class)
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

        indicator.addTab(indicator.newTab().setText(getString(com.tokopedia.core.R.string.title_inbox_message_all)));
        indicator.addTab(indicator.newTab().setText(getString(com.tokopedia.core.R.string.title_inbox_sent)));
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
        mReceiver = new InboxMessageResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {

    }

    public PagerAdapter getViewPagerAdapter() {
        return new MessagePagerAdapter(getFragmentManager(), getFragmentList());
    }

    public List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(InboxMessageFragment.createInstance(MESSAGE_ALL));
        fragmentList.add(InboxMessageFragment.createInstance(MESSAGE_SENT));
        fragmentList.add(InboxMessageFragment.createInstance(MESSAGE_ARCHIVE));
        fragmentList.add(InboxMessageFragment.createInstance(MESSAGE_TRASH));
        return fragmentList;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Fragment fragment = ((MessagePagerAdapter) viewPager.getAdapter()).getItem(viewPager.getCurrentItem());

        if (fragment != null && fragment.getActivity() != null) {
            switch (resultCode) {
                case STATUS_SUCCESS:
                    onReceiveResultSuccess(fragment, resultData);
                    break;
                case STATUS_ERROR:
                    onReceiveResultError(fragment, resultData);
                    break;
            }
        } else {
            Intent intent = getIntent();
            intent.putExtra(BUNDLE_POSITION, viewPager.getCurrentItem());
            finish();
            startActivity(intent);
        }
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(EXTRA_TYPE, 0);
        refreshAllFragment();

        switch (type) {
            case ACTION_ARCHIVE_MESSAGE:
                ((InboxMessageFragment) fragment).onSuccessMoveArchive(resultData);
                break;
            case ACTION_UNDO_ARCHIVE_MESSAGE:
                ((InboxMessageFragment) fragment).onSuccessUndoMoveArchive(resultData);
                break;
            case ACTION_MOVE_TO_INBOX:
                ((InboxMessageFragment) fragment).onSuccessMoveToInbox(resultData);
                break;
            case ACTION_UNDO_MOVE_TO_INBOX:
                ((InboxMessageFragment) fragment).onSuccessUndoMoveToInbox(resultData);
                break;
            case ACTION_DELETE_MESSAGE:
                ((InboxMessageFragment) fragment).onSuccessDeleteMessage(resultData);
                break;
            case ACTION_UNDO_DELETE_MESSAGE:
                ((InboxMessageFragment) fragment).onSuccessUndoDeleteMessage(resultData);
                break;
            case ACTION_DELETE_FOREVER:
                ((InboxMessageFragment) fragment).onSuccessDeleteForever(resultData);
                break;
            case ACTION_MARK_AS_READ:
                ((InboxMessageFragment) fragment).onSuccessMarkAsRead(resultData);
                break;
            case ACTION_MARK_AS_UNREAD:
                ((InboxMessageFragment) fragment).onSuccessMarkAsUnread(resultData);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    private void refreshAllFragment() {
        Fragment fragmentInbox = ((MessagePagerAdapter) viewPager.getAdapter()).getItemByTag(MESSAGE_ALL);
        if (fragmentInbox != null)
            ((InboxMessageFragment) fragmentInbox).setMustRefresh(true);
        Fragment fragmentSent = ((MessagePagerAdapter) viewPager.getAdapter()).getItemByTag(MESSAGE_SENT);
        if (fragmentSent != null)
            ((InboxMessageFragment) fragmentSent).setMustRefresh(true);
        Fragment fragmentArchive = ((MessagePagerAdapter) viewPager.getAdapter()).getItemByTag(MESSAGE_ARCHIVE);
        if (fragmentArchive != null)
            ((InboxMessageFragment) fragmentArchive).setMustRefresh(true);
        Fragment fragmentTrash = ((MessagePagerAdapter) viewPager.getAdapter()).getItemByTag(MESSAGE_TRASH);
        if (fragmentTrash != null)
            ((InboxMessageFragment) fragmentTrash).setMustRefresh(true);
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(EXTRA_TYPE, 0);
        switch (type) {
            case ACTION_ARCHIVE_MESSAGE:
                ((InboxMessageFragment) fragment).onFailedMoveArchive(resultData);
                break;
            case ACTION_UNDO_ARCHIVE_MESSAGE:
                ((InboxMessageFragment) fragment).onFailedUndoMoveArchive(resultData);
                break;
            case ACTION_MOVE_TO_INBOX:
                ((InboxMessageFragment) fragment).onFailedMoveToInbox(resultData);
                break;
            case ACTION_UNDO_MOVE_TO_INBOX:
                ((InboxMessageFragment) fragment).onFailedUndoMoveToInbox(resultData);
                break;
            case ACTION_DELETE_MESSAGE:
                ((InboxMessageFragment) fragment).onFailedDeleteMessage(resultData);
                break;
            case ACTION_UNDO_DELETE_MESSAGE:
                ((InboxMessageFragment) fragment).onFailedUndoDeleteMessage(resultData);
                break;
            case ACTION_DELETE_FOREVER:
                ((InboxMessageFragment) fragment).onFailedDeleteForever(resultData);
                break;
            case ACTION_MARK_AS_READ:
                ((InboxMessageFragment) fragment).onFailedMarkAsRead(resultData);
                break;
            case ACTION_MARK_AS_UNREAD:
                ((InboxMessageFragment) fragment).onFailedMarkAsUnread(resultData);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    @Override
    public void archiveMessage(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_ARCHIVE_MESSAGE);
    }

    @Override
    public void undoArchiveMessage(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_UNDO_ARCHIVE_MESSAGE);
    }

    @Override
    public void moveToInbox(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_MOVE_TO_INBOX);
    }

    @Override
    public void undoMoveToInbox(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_UNDO_MOVE_TO_INBOX);
    }

    @Override
    public void deleteMessage(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_DELETE_MESSAGE);
    }

    @Override
    public void undoDeleteMessage(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_UNDO_DELETE_MESSAGE);
    }

    @Override
    public void deleteMessageForever(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_DELETE_FOREVER);
    }

    @Override
    public void markAsRead(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_MARK_AS_READ);
    }

    @Override
    public void markAsUnread(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_MARK_AS_UNREAD);
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

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxMessageActivity.class);
    }
}
