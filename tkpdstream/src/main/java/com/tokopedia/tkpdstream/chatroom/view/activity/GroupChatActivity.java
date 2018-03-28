package com.tokopedia.tkpdstream.chatroom.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
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
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.tokopedia.design.card.ToolTipUtils;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.channel.view.activity.ChannelActivity;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.tkpdstream.chatroom.domain.ConnectionManager;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.view.ShareData;
import com.tokopedia.tkpdstream.chatroom.view.ShareLayout;
import com.tokopedia.tkpdstream.chatroom.view.adapter.tab.GroupChatTabAdapter;
import com.tokopedia.tkpdstream.chatroom.view.fragment.ChannelInfoFragment;
import com.tokopedia.tkpdstream.chatroom.view.fragment.ChannelVoteFragment;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChannelInfoFragmentListener;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.preference.NotificationPreference;
import com.tokopedia.tkpdstream.chatroom.view.presenter.GroupChatPresenter;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.GroupChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.BaseChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.tab.TabViewModel;
import com.tokopedia.tkpdstream.common.applink.ApplinkConstant;
import com.tokopedia.tkpdstream.common.design.CloseableBottomSheetDialog;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.util.StreamAnalytics;
import com.tokopedia.tkpdstream.common.util.TextFormatter;
import com.tokopedia.tkpdstream.common.util.TransparentStatusBarHelper;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatActivity extends BaseSimpleActivity
        implements GroupChatTabAdapter.TabListener, GroupChatContract.View,
        LoginGroupChatUseCase.LoginGroupChatListener, ChannelHandlerUseCase.ChannelHandlerListener
        , ToolTipUtils.ToolTipListener {

    private static final long VIBRATE_LENGTH = TimeUnit.SECONDS.toMillis(1);
    private static final long KICK_TRESHOLD_TIME = TimeUnit.MINUTES.toMillis(15);
    private static final long TOOLTIP_DELAY = 1500L;

    private static final int KEYBOARD_TRESHOLD = 100;
    private static final int CHATROOM_FRAGMENT = 0;
    private static final int CHANNEL_VOTE_FRAGMENT = 1;
    private static final int CHANNEL_INFO_FRAGMENT = 2;

    public static final String EXTRA_CHANNEL_UUID = "CHANNEL_UUID";
    public static final String EXTRA_CHANNEL_INFO = "CHANNEL_INFO";
    public static final String EXTRA_SHOW_BOTTOM_DIALOG = "SHOW_BOTTOM";
    public static final String ARGS_VIEW_MODEL = "GC_VIEW_MODEL";
    public static final String INITIAL_FRAGMENT = "init_fragment";
    private static final int REQUEST_LOGIN = 101;
    public static final String VOTE = "vote";
    public static final String VOTE_ANNOUNCEMENT = "vote_announcement";
    public static final String VOTE_TYPE = "vote_type";
    private static final String TOTAL_VIEW = "total_view";
    private String voteType;

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

    public View rootView, loading, main;
    private Toolbar toolbar;
    private ImageView channelBanner;
    private RecyclerView tabs;
    private GroupChatTabAdapter tabAdapter;
    private CloseableBottomSheetDialog channelInfoDialog;
    private View sponsorLayout;
    private ImageView sponsorImage;

    private int initialFragment;
    private GroupChatViewModel viewModel;

    private CallbackManager callbackManager;
    private OpenChannel mChannel;

    @Inject
    GroupChatPresenter presenter;

    @Inject
    StreamAnalytics analytics;

    @Inject
    NotificationPreference notificationPreference;

    SharedPreferences sharedPreferences;

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

        callbackManager = CallbackManager.Factory.create();

        initView();
        initInjector();
        initData();
        initPreference();
    }

    private void initInjector() {
        StreamComponent streamComponent = DaggerStreamComponent.builder().baseAppComponent(
                ((BaseMainApplication) getApplication()).getBaseAppComponent()).build();

        DaggerChatroomComponent.builder()
                .streamComponent(streamComponent)
                .build().inject(this);


        presenter.attachView(this);
    }

    private void initView() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        setupToolbar();

        loading = findViewById(R.id.loading);
        main = findViewById(R.id.main_content);

        channelInfoDialog = CloseableBottomSheetDialog.createInstance(this);
        channelInfoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        sponsorLayout = findViewById(R.id.sponsor_layout);
        sponsorImage = findViewById(R.id.sponsor_image);
    }

    private void initData() {
        userSession = ((AbstractionRouter) getApplication()).getSession();
        presenter.getChannelInfo(viewModel.getChannelUuid());
        showLoading();
    }

    private void initPreference() {
        if (userSession != null
                && !TextUtils.isEmpty(userSession.getUserId())
                && getApplication() instanceof StreamModuleRouter) {

            sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getContext());

            String NOTIFICATION_GROUP_CHAT =
                    ((StreamModuleRouter) getApplication()).getNotificationPreferenceConstant();

            boolean isNotificationOn =
                    sharedPreferences.getBoolean(NOTIFICATION_GROUP_CHAT, false);

            if (!isNotificationOn
                    && notificationPreference.isFirstTimeUser(userSession.getUserId())) {
                sharedPreferences.edit().putBoolean(NOTIFICATION_GROUP_CHAT, true).apply();
            }

            notificationPreference.setFirstTime(userSession.getUserId());
        }
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
        if (isLollipopOrNewer()) {
            TransparentStatusBarHelper.assistActivity(this);
        }
        removePaddingStatusBar();

        toolbar = findViewById(R.id.toolbar);
        channelBanner = findViewById(R.id.channel_banner);

        if (isLollipopOrNewer()) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private boolean isLollipopOrNewer() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
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

        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    //
    private void setupViewPager() {
        tabs = findViewById(R.id.tab);
        tabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tabAdapter = GroupChatTabAdapter.createInstance(this, createListFragment());
        tabs.setAdapter(tabAdapter);
    }

    private List<TabViewModel> createListFragment() {
        List<TabViewModel> list = new ArrayList<>();
        list.add(new TabViewModel(getString(R.string.title_group_chat)));
        if (checkPollValid()) {
            list.add(new TabViewModel(getString(R.string.title_vote)));
        }
        list.add(new TabViewModel(getString(R.string.title_info)));
        return list;
    }

    private void showFragment(int fragmentPosition) {

        this.initialFragment = fragmentPosition;
        tabAdapter.setActiveFragment(fragmentPosition);
        tabAdapter.change(fragmentPosition, false);
        switch (fragmentPosition) {
            case CHATROOM_FRAGMENT:
                showChatroomFragment(mChannel);
                break;
            case CHANNEL_VOTE_FRAGMENT:
                if (checkPollValid()) {
                    showChannelVoteFragment();
                } else {
                    showChannelInfoFragment();
                }
                break;
            case CHANNEL_INFO_FRAGMENT:
                showChannelInfoFragment();
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

    private void showChannelInfoFragment() {
        Fragment fragment = populateChannelInfoFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    private void showChannelVoteFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        bundle.putParcelable(VOTE, viewModel.getChannelInfoViewModel().getVoteInfoViewModel());

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (ChannelVoteFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = ChannelVoteFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
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
     * @param context          activity context
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
     * @param context   activity context
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
        if (requestCode == REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.removeEmptyState(rootView);
            initData();
            setUserNameOnReplyText();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
                userSession.getName(), userSession.getProfilePicture(), this, channelInfoViewModel.getSendBirdToken());

        Intent intent = new Intent();
        intent.putExtra(TOTAL_VIEW, channelInfoViewModel.getTotalView());
        setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void updateVoteViewModel(VoteInfoViewModel voteInfoViewModel, String voteType) {
        if (viewModel != null
                && viewModel.getChannelInfoViewModel() != null
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel() != null) {
            if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FINISH
                    || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_FINISH
                    || voteType.equals(VoteAnnouncementViewModel.POLLING_UPDATE)) {
                boolean isVoted = viewModel.getChannelInfoViewModel().getVoteInfoViewModel()
                        .isVoted();
                List<Visitable> tempListOption = new ArrayList<>();
                tempListOption.addAll(viewModel.getChannelInfoViewModel().getVoteInfoViewModel()
                        .getListOption());
                for (int i = 0; i < voteInfoViewModel.getListOption().size(); i++) {
                    if (voteInfoViewModel.getListOption().get(i) instanceof VoteViewModel) {
                        ((VoteViewModel) voteInfoViewModel.getListOption().get(i)).setSelected(
                                ((VoteViewModel) (tempListOption.get(i))).getSelected());
                    }
                }
                voteInfoViewModel.setVoted(isVoted);
                viewModel.getChannelInfoViewModel().setVoteInfoViewModel(voteInfoViewModel);

            } else {
                viewModel.getChannelInfoViewModel().setVoteInfoViewModel(voteInfoViewModel);
            }
        } else if (viewModel != null && viewModel.getChannelInfoViewModel() != null) {
            viewModel.getChannelInfoViewModel().setVoteInfoViewModel(voteInfoViewModel);
        }
    }

    @Override
    public void setChannelHandler() {
        if (viewModel != null && !TextUtils.isEmpty(viewModel.getChannelUrl()))
            presenter.setHandler(viewModel.getChannelUrl(), this);

    }

    @Override
    public void showInfoDialog() {
        channelInfoDialog.setContentView(
                createBottomSheetView(
                        checkPollValid(),
                        viewModel.getChannelInfoViewModel().getChannelViewModel()));

        if (getIntent() != null
                && getIntent().getExtras() != null
                && getIntent().getExtras().getBoolean(GroupChatActivity.EXTRA_SHOW_BOTTOM_DIALOG, false)) {
            channelInfoDialog.show();
        }
    }

    private void setChannelInfoView(ChannelInfoViewModel channelInfoViewModel) {
        this.viewModel.setChannelInfo(channelInfoViewModel);

        setToolbarData(channelInfoViewModel.getTitle(),
                channelInfoViewModel.getBannerUrl(),
                channelInfoViewModel.getTotalView());
        setSponsorData();
        if (channelInfoViewModel.getVoteInfoViewModel().getStatusId() == VoteInfoViewModel.STATUS_ACTIVE
                || channelInfoViewModel.getVoteInfoViewModel().getStatusId() == VoteInfoViewModel.STATUS_FORCE_ACTIVE) {
            setTooltip();
        }
    }

    private void setTooltip() {
        if (checkPollValid()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showTooltip();
                }
            }, TOOLTIP_DELAY);
        }
    }

    private void showTooltip() {
        if (tabs != null
                && tabAdapter != null
                && tabAdapter.getItemCount() > 1
                && tabs.getChildAt(CHANNEL_VOTE_FRAGMENT) != null) {
            ToolTipUtils.showToolTip(ToolTipUtils.setToolTip(this, R.layout.tooltip, this),
                    tabs.getChildAt(CHATROOM_FRAGMENT));
        }
    }

    private boolean checkPollValid() {
        if (viewModel != null
                && viewModel.getChannelInfoViewModel() != null
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel() != null) {
            VoteInfoViewModel voteInfoViewModel = viewModel.getChannelInfoViewModel()
                    .getVoteInfoViewModel();
            return viewModel.getChannelInfoViewModel().isHasPoll()
                    && voteInfoViewModel.getStartTime() != 0
                    && voteInfoViewModel.getEndTime() != 0
                    && voteInfoViewModel.getStartTime() < voteInfoViewModel.getEndTime();
        } else {
            return false;
        }
    }

    private View createBottomSheetView(boolean hasValidPoll, ChannelViewModel channelViewModel) {
        View view = getLayoutInflater().inflate(R.layout.channel_info_bottom_sheet_dialog, null);

        TextView actionButton = view.findViewById(R.id.action_button);
        ImageView image = view.findViewById(R.id.product_image);
        ImageView profile = view.findViewById(R.id.prof_pict);
        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);
        TextView name = view.findViewById(R.id.name);
        TextView participant = view.findViewById(R.id.participant);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channelInfoDialog.dismiss();
                analytics.eventClickJoin();
            }
        });
        if (hasValidPoll)
            actionButton.setText(R.string.lets_vote);
        else
            actionButton.setText(R.string.lets_chat);

        participant.setText(TextFormatter.format(String.valueOf(channelViewModel.getTotalView())));
        name.setText(channelViewModel.getAdminName());
        title.setText(channelViewModel.getTitle());
        subtitle.setText(channelViewModel.getDescription());

        ImageHandler.loadImage2(image, channelViewModel.getImage(), R.drawable.loading_page);
        ImageHandler.loadImageCircle2(profile.getContext(), profile, channelViewModel.getAdminPicture(), R
                .drawable.loading_page);

        return view;
    }

    private void setToolbarData(String title, String bannerUrl, String totalParticipant) {
        toolbar.setTitle(title);
        ImageHandler.LoadImage(channelBanner, bannerUrl);
        setToolbarParticipantCount(totalParticipant);
        setVisibilityHeader(View.VISIBLE);

    }

    private void setToolbarParticipantCount(String totalParticipant) {
        String textParticipant = String.format("%s %s", totalParticipant, getString(R.string.view));
        toolbar.setSubtitle(textParticipant);
    }

    private void setSponsorData() {
        if (!TextUtils.isEmpty(viewModel.getChannelInfoViewModel().getAdsImageUrl())) {
            sponsorLayout.setVisibility(View.VISIBLE);
            ImageHandler.loadImage2(sponsorImage,
                    viewModel.getChannelInfoViewModel().getAdsImageUrl(),
                    R.drawable.loading_page);
            sponsorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventClickComponent(StreamAnalytics.COMPONENT_BANNER, viewModel
                            .getChannelInfoViewModel().getBannerName(), StreamAnalytics
                            .ATTRIBUTE_BANNER);
                    openSponsor(generateAttributeApplink(
                            viewModel.getChannelInfoViewModel().getAdsLink(),
                            StreamAnalytics.ATTRIBUTE_BANNER,
                            viewModel.getChannelUrl(),
                            viewModel.getChannelName()));
                }
            });
        } else {
            sponsorLayout.setVisibility(View.GONE);
        }
    }

    private void openSponsor(String adsLink) {
        ((StreamModuleRouter) getApplicationContext()).openRedirectUrl(this, adsLink);
    }

    @Override
    protected void onResume() {
        super.onResume();

        kickIfIdleForTooLong();

        ConnectionManager.addConnectionManagementHandler(userSession.getUserId(), ConnectionManager
                .CONNECTION_HANDLER_ID, new
                ConnectionManager.ConnectionManagementHandler() {
                    @Override
                    public void onConnected(boolean reconnect) {
                        if (reconnect || (viewModel != null && viewModel.getChannelInfoViewModel()
                                != null)) {
                            presenter.refreshChannelInfo(viewModel.getChannelUuid());
                        }
                    }
                });

    }


    @Override
    public void onSuccessRefreshChannelInfo(ChannelInfoViewModel channelInfoViewModel) {
        setChannelInfoView(channelInfoViewModel);

        if (currentFragmentIsChat()) {
            refreshChat();
        } else if (currentFragmentIsInfo()) {
            populateChannelInfoFragment();
        }
    }

    private void refreshChat() {
        ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                (GroupChatFragment.class.getSimpleName())).refreshChat();
    }

    private Fragment populateChannelInfoFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                ChannelInfoFragment.class.getSimpleName());

        if (fragment == null) {
            fragment = ChannelInfoFragment.createInstance(bundle);
        }

        ((ChannelInfoFragmentListener.View) fragment).renderData(
                viewModel.getChannelInfoViewModel().getChannelViewModel());

        return fragment;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (viewModel != null) {
            viewModel.setTimeStampBeforePause(System.currentTimeMillis());
        }
        ConnectionManager.removeConnectionManagementHandler(ConnectionManager.CONNECTION_HANDLER_ID);
        SendBird.removeChannelHandler(ConnectionManager.CHANNEL_HANDLER_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.logoutChannel(mChannel);
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
            setupViewPager();
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
                        GroupChatActivity.this, viewModel.getChannelInfoViewModel().getSendBirdToken());
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
                getSupportFragmentManager().findFragmentById(R.id.container) instanceof GroupChatFragment;
    }

    private boolean currentFragmentIsVote() {
        return getSupportFragmentManager().findFragmentById(R.id.container) != null &&
                getSupportFragmentManager().findFragmentById(R.id.container) instanceof ChannelVoteFragment;
    }

    private boolean currentFragmentIsInfo() {
        return getSupportFragmentManager().findFragmentById(R.id.container) != null &&
                getSupportFragmentManager().findFragmentById(R.id.container) instanceof ChannelInfoFragment;
    }

    private void setUserNameOnReplyText() {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).setReplyTextHint();

        }
    }

    @Override
    public void onMessageReceived(Visitable map) {
        if (map instanceof VoteAnnouncementViewModel) {
            VoteAnnouncementViewModel voteAnnouncementViewModel = ((VoteAnnouncementViewModel) map);
            handleVoteAnnouncement(voteAnnouncementViewModel, voteAnnouncementViewModel.getVoteType());
        } else if (map instanceof SprintSaleAnnouncementViewModel) {
            updateSprintSaleData((SprintSaleAnnouncementViewModel) map);
        }


        if (map instanceof BaseChatViewModel
                && ((BaseChatViewModel) map).isCanVibrate()) {
            vibratePhone();
        }

        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onMessageReceived(map);
        } else if (currentFragmentIsVote()) {
            ((ChannelVoteFragment) getSupportFragmentManager().findFragmentByTag
                    (ChannelVoteFragment.class.getSimpleName())).onMessageReceived(map);
        }
    }

    @Override
    public void vibratePhone() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_LENGTH, VibrationEffect
                        .DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(VIBRATE_LENGTH);
            }
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
                    (GroupChatFragment.class.getSimpleName())).onUserEntered(userActionViewModel
            );
        }

        try {
            if (!userActionViewModel.getUserId().equalsIgnoreCase(userSession.getUserId())) {
                viewModel.setTotalView(String.valueOf(Integer.parseInt(viewModel.getTotalView()) +
                        1));
            }
            setToolbarParticipantCount(viewModel.getTotalView());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserExited(UserActionViewModel userActionViewModel, String participantCount) {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onUserExited(userActionViewModel
            );
        }
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

    @Override
    public void setView(View view) {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void handleVoteAnnouncement(VoteAnnouncementViewModel messageItem, String voteType) {
        VoteInfoViewModel voteInfoViewModel = messageItem.getVoteInfoViewModel();
        updateVoteViewModel(messageItem.getVoteInfoViewModel(), voteType);

        if ((voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_ACTIVE
                || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_ACTIVE)
                && tabAdapter.getItemCount() < 3) {
            tabAdapter.add(CHANNEL_VOTE_FRAGMENT, new TabViewModel(getString(R.string
                    .title_vote)));
            setTooltip();
            tabAdapter.notifyItemInserted(CHANNEL_VOTE_FRAGMENT);
        } else if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_CANCELED) {
            tabAdapter.remove(CHANNEL_VOTE_FRAGMENT);
            tabAdapter.notifyItemRemoved(CHANNEL_VOTE_FRAGMENT);
        }

        if (!currentFragmentIsVote() && voteInfoViewModel.getStatusId() != VoteInfoViewModel.STATUS_CANCELED) {
            tabAdapter.change(CHANNEL_VOTE_FRAGMENT, true);
            if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_ACTIVE
                    && voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_ACTIVE) {
                setTooltip();
            }
        }

    }

    public void moveToVoteFragment() {
        showFragment(CHANNEL_VOTE_FRAGMENT);
    }

    public String getToolbarTitle() {
        return toolbar.getTitle().toString();
    }

    @Override
    public SprintSaleViewModel getSprintSaleViewModel() {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null && viewModel
                .getChannelInfoViewModel().getSprintSaleViewModel() != null) {
            return viewModel.getChannelInfoViewModel().getSprintSaleViewModel();
        } else {
            return null;
        }
    }

    @Override

    public ChannelInfoViewModel getChannelInfoViewModel() {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null) {
            return viewModel.getChannelInfoViewModel();
        } else {
            return null;
        }
    }

    @Override
    public void eventClickComponent(String componentName, String campaignName, String attributeName) {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null) {
            analytics.eventClickComponent(componentName, campaignName, attributeName, viewModel
                    .getChannelUrl(), viewModel.getChannelName());
        }
    }

    @Override
    public void updateSprintSaleData(SprintSaleAnnouncementViewModel messageItem) {
        if (this.viewModel != null
                && this.viewModel.getChannelInfoViewModel() != null
                && this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel() != null) {
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setCampaignName(messageItem.getCampaignName());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setListProduct(messageItem.getListProducts());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setEndDate(messageItem.getEndDate());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setStartDate(messageItem.getStartDate());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setRedirectUrl(messageItem.getRedirectUrl());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setSprintSaleType(messageItem.getSprintSaleType());
        } else if (this.viewModel != null
                && this.viewModel.getChannelInfoViewModel() != null) {
            this.viewModel.getChannelInfoViewModel().setSprintSaleViewModel(new SprintSaleViewModel(
                    messageItem.getListProducts(),
                    messageItem.getCampaignName(),
                    messageItem.getStartDate(),
                    messageItem.getEndDate(),
                    messageItem.getRedirectUrl(),
                    messageItem.getSprintSaleType()));
        }
    }

    @Override
    public String generateAttributeApplink(String applink,
                                           String attributeBanner) {
        if (viewModel != null) {
            return generateAttributeApplink(applink, attributeBanner,
                    viewModel.getChannelUrl(),
                    viewModel.getChannelName());
        } else {
            return applink;
        }
    }

    private String generateAttributeApplink(String applink,
                                            String attributeBanner,
                                            String channelUrl,
                                            String channelName) {
        if (applink.contains("?")) {
            return String.format(applink + "&%s", generateTrackerAttribution(attributeBanner,
                    channelUrl, channelName));
        } else {
            return String.format(applink + "?%s", generateTrackerAttribution(attributeBanner,
                    channelUrl, channelName));
        }
    }

    private String generateTrackerAttribution(String attributeBanner,
                                              String channelUrl,
                                              String channelName) {
        return "tracker_attribution=" + StreamAnalytics.generateTrackerAttribution
                (attributeBanner, channelUrl, channelName);
    }
}
