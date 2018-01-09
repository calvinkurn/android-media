package com.tokopedia.inbox.inboxmessageold.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.inbox.inboxmessageold.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessageold.fragment.InboxMessageDetailFragment;
import com.tokopedia.inbox.inboxmessageold.intentservice.InboxMessageIntentService;
import com.tokopedia.inbox.inboxmessageold.intentservice.InboxMessageResultReceiver;

/**
 * Created by Nisie on 5/19/16.
 */
public class InboxMessageDetailActivity extends BasePresenterActivity
        implements InboxMessageDetailFragment.DoActionInboxMessageListener,
        InboxMessageConstant, InboxMessageResultReceiver.Receiver {


    private static final String TAG = "INBOX_MESSAGE_DETAIL_FRAGMENT";
    InboxMessageResultReceiver mReceiver;

    @DeepLink(Constants.Applinks.MESSAGE_DETAIL)
    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        Intent detailsIntent = new Intent(context, InboxMessageDetailActivity.class).putExtras(extras);
        Intent parentIntent = new Intent(context, InboxMessageActivity.class);
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
        return com.tokopedia.core.R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        getFragmentManager().beginTransaction()
                .add(com.tokopedia.core.R.id.container, InboxMessageDetailFragment.createInstance(getIntent().getExtras()),
                        TAG)
                .commit();
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

    @Override
    public void sendReply(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_SEND_REPLY);
    }

    @Override
    public void flagSpam(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_FLAG_SPAM);
    }

    @Override
    public void undoFlagSpam(Bundle param) {
        InboxMessageIntentService.startAction(this,
                param, mReceiver, ACTION_UNDO_FLAG_SPAM);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG);

        if (fragment != null) {
            switch (resultCode) {
                case STATUS_SUCCESS:
                    onReceiveResultSuccess(fragment, resultData);
                    break;
                case STATUS_ERROR:
                    onReceiveResultError(fragment, resultData);
                    break;
            }
        }
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(EXTRA_TYPE, 0);
        switch (type) {
            case ACTION_SEND_REPLY:
                ((InboxMessageDetailFragment) fragment).onSuccessSendReply(resultData);
                break;
            case ACTION_FLAG_SPAM:
                ((InboxMessageDetailFragment) fragment).onSuccessFlagSpam(resultData);
                break;
            case ACTION_UNDO_FLAG_SPAM:
                ((InboxMessageDetailFragment) fragment).onSuccessUndoFlagSpam(resultData);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(EXTRA_TYPE, 0);
        switch (type) {
            case ACTION_SEND_REPLY:
                ((InboxMessageDetailFragment) fragment).onFailedSendReply(resultData);
                break;
            case ACTION_FLAG_SPAM:
                ((InboxMessageDetailFragment) fragment).onFailedFlagSpam(resultData);
                break;
            case ACTION_UNDO_FLAG_SPAM:
                ((InboxMessageDetailFragment) fragment).onFailedUndoFlagSpam(resultData);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
