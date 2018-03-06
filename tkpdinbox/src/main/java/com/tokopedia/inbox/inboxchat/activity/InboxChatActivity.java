package com.tokopedia.inbox.inboxchat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatNotifInterface;
import com.tokopedia.inbox.inboxchat.adapter.IndicatorAdapter;
import com.tokopedia.inbox.inboxchat.fragment.InboxChatFragment;
import com.tokopedia.inbox.inboxchat.viewmodel.IndicatorItem;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessageold.activity.InboxMessageActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InboxChatActivity extends DrawerPresenterActivity
        implements InboxMessageConstant, NotificationReceivedListener, HasComponent,
        ChatNotifInterface, IndicatorAdapter.OnIndicatorClickListener {

    private static final int POSITION_TOP_CHAT = 0;
    private static final int POSITION_GROUP_CHAT = 1;

    private static final String ACTIVE_INDICATOR_POSITION = "active";
    private static final int REQUEST_LOGIN = 101;
    IndicatorAdapter indicatorAdapter;
    RecyclerView indicator;

    @Inject
    SessionHandler sessionHandler;

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
        if (remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT)) {
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
        return R.layout.activity_inbox_chat;
    }

    @Override
    protected void initView() {
        super.initView();

        indicator = findViewById(R.id.indicator);
        indicatorAdapter = IndicatorAdapter.createInstance(getIndicatorList(), this);
        indicator.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .HORIZONTAL, false));
        indicator.addItemDecoration(new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.step_size_nob)));
        indicator.setAdapter(indicatorAdapter);

        if (getIntent().getExtras() != null
                && getIntent().getExtras().getInt(ACTIVE_INDICATOR_POSITION, -1) != -1) {
            indicatorAdapter.setActiveIndicator(getIntent().getExtras().getInt
                    (ACTIVE_INDICATOR_POSITION, 0));
            initGroupChatFragment();
        } else {
            initTopChatFragment();
        }

    }

    private List<IndicatorItem> getIndicatorList() {
        List<IndicatorItem> list = new ArrayList<>();
        list.add(new IndicatorItem(getString(R.string.title_personal), R.drawable
                .ic_indicator_topchat, true));
        list.add(new IndicatorItem(getString(R.string.title_community), R.drawable
                .ic_indicator_channel, false));
        return list;
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
    }

    @Override
    public void onSuccessGetTopChatNotification(int notifUnreads) {

        if (notifUnreads > 0) {
            TextView titleTextView = (TextView) toolbar.findViewById(R.id.actionbar_title);
            titleTextView.setText("Chat (" + notifUnreads + ")");
        }

        indicatorAdapter.setNotification(POSITION_TOP_CHAT, notifUnreads);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxChatActivity.class);
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    public void onIndicatorClicked(int position) {
        indicatorAdapter.setActiveIndicator(position);
        switch (position) {
            case POSITION_TOP_CHAT:
                initTopChatFragment();
                break;
            case POSITION_GROUP_CHAT:
                initGroupChatFragment();
                break;
            default:
        }
    }

    private void initGroupChatFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        TkpdInboxRouter inboxRouter = (TkpdInboxRouter) getApplicationContext();

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (inboxRouter.getChannelFragmentTag());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = inboxRouter.getChannelFragment(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    private void initTopChatFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (InboxChatFragment.class.getSimpleName());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = InboxChatFragment.createInstance(MESSAGE_ALL);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public static Intent getChannelCallingIntent(Context context) {
        Intent intent = new Intent(context, InboxChatActivity.class);
        intent.putExtra(ACTIVE_INDICATOR_POSITION, POSITION_GROUP_CHAT);
        return intent;
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        SpaceItemDecoration(int verticalSpaceHeight) {
            this.space = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {

            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left = space / 2;
            } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                outRect.right = space / 2;
            } else {
                outRect.right = space / 2;
                outRect.left = space / 2;
            }

        }
    }
}
