package com.tokopedia.inbox.inboxmessage.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessage.fragment.InboxMessageDetailFragment;
import com.tokopedia.inbox.inboxmessage.intentservice.InboxMessageIntentService;
import com.tokopedia.inbox.inboxmessage.intentservice.InboxMessageResultReceiver;

import static com.tokopedia.core.inboxreputation.InboxReputationConstant.PARAM_POSITION;
import static com.tokopedia.core.otp.domain.interactor.RequestOtpUseCase.PARAM_MODE;

/**
 * Created by Nisie on 5/19/16.
 */
public class InboxMessageDetailActivity extends BasePresenterActivity
        implements InboxMessageDetailFragment.DoActionInboxMessageListener,
        InboxMessageConstant, InboxMessageResultReceiver.Receiver, NotificationReceivedListener {


    private static final String TAG = "INBOX_MESSAGE_DETAIL_FRAGMENT";
    public static final String PARAM_SENDER_ROLE = "PARAM_SENDER_ROLE";

    InboxMessageResultReceiver mReceiver;

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
        Drawable upArrow = ContextCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
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

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    public static Intent getCallingIntent(Context context, String nav, String messageId,
                                          int position, String senderName, String senderTag,
                                          String senderId, String role, int mode) {
        Intent intent = new Intent(context, InboxMessageDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NAV, nav);
        bundle.putParcelable(PARAM_MESSAGE, null);
        bundle.putString(PARAM_MESSAGE_ID, messageId);
        bundle.putInt(PARAM_POSITION, position);
        bundle.putString(PARAM_SENDER_NAME, senderName);
        bundle.putString(PARAM_SENDER_TAG, senderTag);
        bundle.putString(PARAM_SENDER_ID, senderId);
        bundle.putString(PARAM_SENDER_ROLE, role);
        bundle.putInt(PARAM_MODE, mode);
        intent.putExtras(bundle);
        return intent;
    }
}
