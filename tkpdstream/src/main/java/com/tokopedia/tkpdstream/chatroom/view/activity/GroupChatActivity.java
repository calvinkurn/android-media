package com.tokopedia.tkpdstream.chatroom.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.CallbackManager;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.channel.view.activity.ChannelActivity;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.domain.ConnectionManager;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.view.ShareData;
import com.tokopedia.tkpdstream.chatroom.view.ShareLayout;
import com.tokopedia.tkpdstream.chatroom.view.adapter.tab.GroupChatTabAdapter;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.presenter.GroupChatPresenter;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.GroupChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.tab.TabViewModel;
import com.tokopedia.tkpdstream.common.applink.ApplinkConstant;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.util.StreamAnalytics;
import com.tokopedia.tkpdstream.common.util.TransparentStatusBarHelper;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.chatroom.di.DaggerChatroomComponent;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatActivity extends BaseSimpleActivity
        implements GroupChatTabAdapter.TabListener, GroupChatContract.View,
        LoginGroupChatUseCase.LoginGroupChatListener, ChannelHandlerUseCase.ChannelHandlerListener {

    private static final int KEYBOARD_TRESHOLD = 100;
    private static final int CHATROOM_FRAGMENT = 0;
    private static final long KICK_TRESHOLD_TIME = TimeUnit.MINUTES.toMillis(15);


    public static final String EXTRA_CHANNEL_UUID = "CHANNEL_UUID";
    public static final String EXTRA_CHANNEL_INFO = "CHANNEL_INFO";
    public static final String EXTRA_SHOW_BOTTOM_DIALOG = "SHOW_BOTTOM";
    public static final String ARGS_VIEW_MODEL = "GC_VIEW_MODEL";
    public static final String INITIAL_FRAGMENT = "init_fragment";

    @DeepLink(ApplinkConstant.GROUPCHAT_ROOM)
    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        String id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID);
        Intent homeIntent = ((StreamModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        Intent detailsIntent = GroupChatActivity.getCallingIntent(context, id);
        Intent parentIntent = ((StreamModuleRouter) context.getApplicationContext())
                .getInboxChannelsIntent(context);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

    @DeepLink(ApplinkConstant.GROUPCHAT_LIST)
    public static TaskStackBuilder getCallingTaskStackList(Context context, Bundle extras) {
        String id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID);
        Intent homeIntent = ((StreamModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        Intent detailsIntent = GroupChatActivity.getCallingIntent(context, id);
        Intent parentIntent = ((StreamModuleRouter) context.getApplicationContext())
                .getInboxChannelsIntent(context);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

    @DeepLink(ApplinkConstant.GROUPCHAT_ROOM_VIA_LIST)
    public static TaskStackBuilder getCallingTaskStackViaList(Context context, Bundle extras) {
        String id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID);
        Intent homeIntent = ((StreamModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        Intent detailsIntent = GroupChatActivity.getCallingIntent(context, id);
        Intent parentIntent = ((StreamModuleRouter) context.getApplicationContext())
                .getInboxChannelsIntent(context);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

//
//    private TouchViewPager viewPager;
//    private GroupChatViewPagerAdapter pagerAdapter;

    public View rootView, loading, main;
    private Toolbar toolbar;
    private ImageView channelBanner;
    private RecyclerView tabs;
    private GroupChatTabAdapter tabAdapter;

    private int initialFragment;
    private GroupChatViewModel viewModel;

    private CallbackManager callbackManager;
    private OpenChannel mChannel;

    @Inject
    GroupChatPresenter presenter;

    @Inject
    StreamAnalytics analytics;

    private UserSession userSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            initialFragment = savedInstanceState.getInt(INITIAL_FRAGMENT, CHATROOM_FRAGMENT);
        } else if (getIntent().getExtras() != null) {
            initialFragment = getIntent().getExtras().getInt(INITIAL_FRAGMENT, CHATROOM_FRAGMENT);
        } else {
            initialFragment = CHATROOM_FRAGMENT;
        }

        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(ARGS_VIEW_MODEL);
        } else if (getIntent().getExtras() != null) {
            viewModel = new GroupChatViewModel(getIntent().getExtras().getString(GroupChatActivity
                    .EXTRA_CHANNEL_UUID, ""));
        } else {
            Intent intent = new Intent();
            intent.putExtra(ChannelActivity.RESULT_MESSAGE, getString(R.string.default_request_error_unknown));
            setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL, intent);
            finish();
        }

        initView(savedInstanceState);
        initInjector();
        callbackManager = CallbackManager.Factory.create();

        initData();
    }

    private void initInjector() {
        StreamComponent streamComponent = DaggerStreamComponent.builder().baseAppComponent(
                ((BaseMainApplication) getApplication()).getBaseAppComponent()).build();

        DaggerChatroomComponent.builder()
                .streamComponent(streamComponent)
                .build().inject(this);


        presenter.attachView(this);
    }

    private void initView(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        setupToolbar();
        setupViewPager(bundle);

        loading = findViewById(R.id.loading);
        main = findViewById(R.id.main_content);

    }

    private void initData() {
        userSession = ((AbstractionRouter) getApplication()).getSession();
        presenter.getChannelInfo(viewModel.getChannelUuid());
        showLoading();
    }

    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
    }

    public void hideLoading() {
        loading.setVisibility(View.GONE);
        main.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setupStatusBar() {

    }

    private void setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransparentStatusBarHelper.assistActivity(this);
        }
        removePaddingStatusBar();

        toolbar = findViewById(R.id.toolbar);
        channelBanner = findViewById(R.id.channel_banner);

        ViewGroup.LayoutParams params = channelBanner.getLayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
            params.height = getResources().getDimensionPixelSize(R.dimen.channel_banner_height);
        } else {
            params.height = getResources().getDimensionPixelSize(R.dimen
                    .channel_banner_height_without_status);
        }

        channelBanner.setLayoutParams(params);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_chat_room_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            analytics.eventClickShare();
            ShareData shareData = ShareData.Builder.aShareData()
                    .setId(viewModel.getChannelUuid())
                    .setName(viewModel.getChannelName())
                    .setDescription(String.format(getString(R.string.lets_join_channel),
                            viewModel.getChannelName()))
                    .setImgUri(viewModel.getChannelInfoViewModel().getBannerUrl())
                    .setUri(viewModel.getChannelUrl())
                    .setType(ShareData.FEED_TYPE)
                    .build();

            ShareLayout shareLayout = new ShareLayout(
                    this,
                    callbackManager, viewModel.getChannelUrl(),
                    toolbar.getTitle().toString(), analytics);
            shareLayout.setShareModel(shareData);
            shareLayout.show();
            return true;

        } else if (item.getItemId() == R.id.action_info) {
//            boolean temp = checkPollValid(viewModel.getChannelInfoViewModel().isHasPoll(), viewModel.getChannelInfoViewModel().getVoteInfoViewModel());
//            channelInfoDialog.setContentView(createBottomSheetView(temp, viewModel
//                    .getChannelInfoViewModel().getChannelViewModel(), false));
//            channelInfoDialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    //
    private void setupViewPager(Bundle bundle) {
//        viewPager = findViewById(R.id.pager);
//        Tabs tabs = findViewById(R.id.tab);
//        pagerAdapter = GroupChatViewPagerAdapter.createInstance(getSupportFragmentManager());
//        pagerAdapter.addFragment(GroupChatFragment.createInstance(bundle), getString(R.string
//                .title_group_chat));
//        viewPager.setAdapter(pagerAdapter);
//        pagerAdapter.notifyDataSetChanged();
//
//        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                KeyboardHandler.hideSoftKeyboard(GroupChatActivity.this);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                if (tab.getPosition() == CHATROOM_FRAGMENT) {
//                    Fragment fragment = pagerAdapter.getItem(tab.getPosition());
//                    if (fragment != null) {
//                        if (fragment instanceof GroupChatFragment) {
//                            ((GroupChatFragment) fragment).scrollToBottom();
//                        }
//                    }
//                }
//            }
//        });
//
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
//        tabs.setupWithViewPager(viewPager);

        tabs = findViewById(R.id.tab);
        tabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tabAdapter = GroupChatTabAdapter.createInstance(this, createListFragment());
        tabs.setAdapter(tabAdapter);
    }

    private List<TabViewModel> createListFragment() {
        List<TabViewModel> list = new ArrayList<>();
        list.add(new TabViewModel(getString(R.string.title_group_chat)));
        list.add(new TabViewModel(getString(R.string.title_vote)));
        return list;
    }


    private void showFragment(int fragmentPosition) {
//        if (viewPager != null && viewPager.getAdapter().getCount() < initialFragment) {
//            viewPager.setCurrentItem(initialFragment);
//        }

        this.initialFragment = fragmentPosition;
        tabAdapter.setActiveFragment(fragmentPosition);

        switch (fragmentPosition) {
            case CHATROOM_FRAGMENT:
                showChatroomFragment(mChannel);
                break;
            default:
                break;
        }

    }

    private void showChatroomFragment(OpenChannel mChannel) {

        if (mChannel != null) {

            Bundle bundle = new Bundle();
            if (getIntent().getExtras() != null) {
                bundle.putAll(getIntent().getExtras());
            }

            Fragment fragment = getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName());
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (fragment == null) {
                fragment = GroupChatFragment.createInstance(bundle);
            }
            ((GroupChatFragment) fragment).setChannel(mChannel);
            fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    private void removePaddingStatusBar() {

        rootView = findViewById(R.id.root_view);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                if (heightDiff > KEYBOARD_TRESHOLD) {
                    removePaddingIfKeyboardIsShowing();
                } else {
                    addPaddingIfKeyboardIsClosed();
                }
            }
        });
    }

    private void addPaddingIfKeyboardIsClosed() {
        if (getSoftButtonsBarSizePort(GroupChatActivity.this) > 0) {
            FrameLayout container = rootView.findViewById(R.id.container);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container
                    .getLayoutParams();
            params.setMargins(0, 0, 0, getSoftButtonsBarSizePort(GroupChatActivity.this));
            container.setLayoutParams(params);
        }
    }

    private void removePaddingIfKeyboardIsShowing() {
        if (getSoftButtonsBarSizePort(GroupChatActivity.this) > 0) {
            FrameLayout container = rootView.findViewById(R.id.container);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            container.setLayoutParams(params);
        }
    }

    public static int getSoftButtonsBarSizePort(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_group_chat;
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(((StreamModuleRouter) getApplicationContext()).getInboxChannelsIntent(this));
            finish();
        }
        super.onBackPressed();
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        setContentView(getLayoutRes());
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onTabClicked(int position) {
        showFragment(position);
    }

    /**
     * @param context
     * @param channelViewModel only to be used from channel list.
     * @return Intent
     */
    public static Intent getCallingIntent(Context context, ChannelViewModel channelViewModel) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CHANNEL_INFO, channelViewModel);
        bundle.putString(EXTRA_CHANNEL_UUID, channelViewModel.getChannelUrl());
        bundle.putBoolean(EXTRA_SHOW_BOTTOM_DIALOG, false);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * @param context
     * @param channelId can also be substitued by channelUrl
     * @return Intent
     */
    public static Intent getCallingIntent(Context context, String channelId) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CHANNEL_UUID, channelId);
        bundle.putBoolean(EXTRA_SHOW_BOTTOM_DIALOG, true);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onErrorGetChannelInfo(String errorMessage) {
        NetworkErrorHelper.showEmptyState(this, rootView, errorMessage, new NetworkErrorHelper
                .RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                initData();
            }
        });
        setVisibilityHeader(View.GONE);
    }

    void setVisibilityHeader(int visible) {
        toolbar.setVisibility(visible);
        channelBanner.setVisibility(visible);
    }

    @Override
    public void onSuccessGetChannelInfo(ChannelInfoViewModel channelInfoViewModel) {
        setChannelInfoView(channelInfoViewModel);
        presenter.enterChannel(userSession.getUserId(), viewModel.getChannelUrl(),
                userSession.getName(), userSession.getProfilePicture(), this);
    }

    private void setChannelInfoView(ChannelInfoViewModel channelInfoViewModel) {
        this.viewModel.setChannelInfo(channelInfoViewModel);

        setToolbarData(channelInfoViewModel.getTitle(),
                channelInfoViewModel.getBannerUrl(),
                channelInfoViewModel.getTotalParticipantsOnline());


    }

    private void setToolbarData(String title, String bannerUrl, String totalParticipant) {
        toolbar.setTitle(title);
        ImageHandler.loadImageBlur(this, channelBanner, bannerUrl);
        setToolbarParticipantCount(totalParticipant);
        setVisibilityHeader(View.VISIBLE);

    }

    private void setToolbarParticipantCount(String totalParticipant) {
        String textParticipant = String.format("%s %s", totalParticipant, getString(R.string.view));
        toolbar.setSubtitle(textParticipant);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (viewModel != null && !TextUtils.isEmpty(viewModel.getChannelUrl()))
            presenter.setHandler(viewModel.getChannelUrl(), this);

        kickIfIdleForTooLong();

        ConnectionManager.addConnectionManagementHandler(userSession.getUserId(), ConnectionManager
                .CONNECTION_HANDLER_ID, new
                ConnectionManager.ConnectionManagementHandler() {
                    @Override
                    public void onConnected(boolean reconnect) {
                        if (reconnect && currentFragmentIsChat()) {
                            refreshChat();
                        }
                    }
                });


    }

    private void refreshChat() {
        ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                (GroupChatFragment.class.getSimpleName())).refreshChat();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.logoutChannel(mChannel);
        ConnectionManager.removeConnectionManagementHandler(ConnectionManager.CONNECTION_HANDLER_ID);
        SendBird.removeChannelHandler(ConnectionManager.CHANNEL_HANDLER_ID);
    }

    private void kickIfIdleForTooLong() {
        if (viewModel != null) {
            if (viewModel.getTimeStampBeforePause() > 0
                    && System.currentTimeMillis() - viewModel.getTimeStampBeforePause() > KICK_TRESHOLD_TIME) {
                onUserIdleTooLong();
            }
        }
    }

    private void onUserIdleTooLong() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.you_have_been_kicked);
        builder.setMessage(R.string.you_have_been_idle_for_too_long);
        builder.setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onSuccessEnterChannel(OpenChannel openChannel) {
        try {
            hideLoading();
            mChannel = openChannel;
            showFragment(initialFragment);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorEnterChannel(String errorMessage) {
        NetworkErrorHelper.showEmptyState(this, rootView, errorMessage, new NetworkErrorHelper
                .RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.enterChannel(userSession.getUserId(), viewModel.getChannelUuid(),
                        userSession.getName(), userSession.getProfilePicture(),
                        GroupChatActivity.this);
            }
        });
    }

    @Override
    public void onUserBanned(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.default_banned_title);
        builder.setMessage(errorMessage);
        builder.setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onChannelNotFound(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.channel_not_found);
        builder.setMessage(errorMessage);
        builder.setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    private boolean currentFragmentIsChat() {
        return getSupportFragmentManager().findFragmentById(R.id.container) != null &&
                getSupportFragmentManager().findFragmentById(R.id.container) instanceof
                        GroupChatFragment;
    }

    @Override
    public void onMessageReceived(Visitable map) {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onMessageReceived(map);
        }
    }

    @Override
    public void onMessageDeleted(long msgId) {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onMessageDeleted(msgId);
        }
    }

    @Override
    public void onMessageUpdated(Visitable map) {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onMessageUpdated(map);
        }
    }

    @Override
    public void onUserEntered(UserActionViewModel userActionViewModel, String participantCount) {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onUserEntered(userActionViewModel,
                    participantCount);
        }

        try {
            if (!userActionViewModel.getUserId().equalsIgnoreCase(userSession.getUserId())) {
                viewModel.setTotalParticipant(String.valueOf(Integer.parseInt(viewModel.getTotalParticipant()) +
                        1));
            }
            setToolbarParticipantCount(viewModel.getTotalParticipant());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserExited(UserActionViewModel userActionViewModel, String participantCount) {

    }

    @Override
    public void onUserBanned() {
        onUserBanned(getString(R.string.user_is_banned));
    }

    @Override
    public void onChannelDeleted() {
        onChannelNotFound(getString(R.string.channel_has_been_deleted));

    }

    @Override
    public void onChannelFrozen() {
        onChannelNotFound(getString(R.string.channel_deactivated));

    }
}
