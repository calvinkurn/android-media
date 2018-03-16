package com.tokopedia.tkpdstream.chatroom.view.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.channel.data.analytics.ChannelAnalytics;
import com.tokopedia.tkpdstream.channel.view.ProgressBarWithTimer;
import com.tokopedia.tkpdstream.channel.view.activity.ChannelActivity;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.tkpdstream.chatroom.domain.ConnectionManager;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.view.ShareData;
import com.tokopedia.tkpdstream.chatroom.view.ShareLayout;
import com.tokopedia.tkpdstream.chatroom.view.SpaceItemDecoration;
import com.tokopedia.tkpdstream.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.tkpdstream.chatroom.view.adapter.GroupChatAdapter;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactory;
import com.tokopedia.tkpdstream.chatroom.view.adapter.typefactory.GroupChatTypeFactoryImpl;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.presenter.GroupChatPresenter;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.GroupChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.UserActionViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.VoteAnnouncementViewModel;
import com.tokopedia.tkpdstream.common.design.CloseableBottomSheetDialog;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.util.StreamAnalytics;
import com.tokopedia.tkpdstream.common.util.TextFormatter;
import com.tokopedia.tkpdstream.vote.view.adapter.VoteAdapter;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactory;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactoryImpl;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteStatisticViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatFragment extends BaseDaggerFragment implements GroupChatContract.View,
        ChannelHandlerUseCase.ChannelHandlerListener, LoginGroupChatUseCase.LoginGroupChatListener,
        ProgressBarWithTimer.Listener, GroupChatContract.View.ImageViewHolderListener, GroupChatContract.View.VoteAnnouncementViewHolderListener {

    public static final String ARGS_VIEW_MODEL = "GC_VIEW_MODEL";
    private static final int REQUEST_LOGIN = 101;
    private static final long KICK_TRESHOLD_TIME = TimeUnit.MINUTES.toMillis(15);
    private static final long DELAY_TIME = 2000L;

    @Inject
    GroupChatPresenter presenter;

    @Inject
    StreamAnalytics analytics;

    private Toolbar toolbar;
    private ImageView channelBanner;
    private RecyclerView chatRecyclerView;
    private RecyclerView voteRecyclerView;
    private EditText replyEditText;
    private View sendButton;
    private View voteBar;
    private View voteBody;
    private TextView voteTitle;
    private TextView voteParticipant;
    private TextView voteInfoLink;
    private ImageView iconVote;
    private View votedView;
    private View divider;
    private View main, loading;
    private TextView voteStatus;
    private ImageView arrow;
    private GroupChatAdapter adapter;
    private VoteAdapter voteAdapter;
    private LinearLayoutManager layoutManager;
    private ProgressBarWithTimer progressBarWithTimer;
    private View chatNotificationView;

    private OpenChannel mChannel;
    private PreviousMessageListQuery mPrevMessageListQuery;
    private GroupChatViewModel viewModel;
    private UserSession userSession;
    private String channelUrl;

    private CloseableBottomSheetDialog channelInfoDialog;

    int newMessageCounter;
    private ShareLayout shareLayout;

    private CallbackManager callbackManager;
    private TextWatcher replyTextWatcher;

    @Override
    protected String getScreenName() {
        return ChannelAnalytics.Screen.CHAT_ROOM;
    }

    @Override
    protected void initInjector() {
        StreamComponent streamComponent = DaggerStreamComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()).build();

        DaggerChatroomComponent.builder()
                .streamComponent(streamComponent)
                .build().inject(this);


        presenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new GroupChatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(ARGS_VIEW_MODEL);
        } else if (getArguments() != null) {
            viewModel = new GroupChatViewModel(getArguments().getString(GroupChatActivity
                    .EXTRA_CHANNEL_UUID, ""));
        } else {
            Intent intent = new Intent();
            intent.putExtra(ChannelActivity.RESULT_MESSAGE, getString(R.string.default_request_error_unknown));
            getActivity().setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL, intent);
            getActivity().finish();
        }

        userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();

        if (userSession != null && !userSession.isLoggedIn()) {
            startActivityForResult(((StreamModuleRouter) getActivity().getApplicationContext())
                    .getLoginIntent
                            (getActivity()), REQUEST_LOGIN);
        }
        callbackManager = CallbackManager.Factory.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat_room, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        channelBanner = view.findViewById(R.id.channel_banner);
        chatRecyclerView = view.findViewById(R.id.chat_list);
        voteRecyclerView = view.findViewById(R.id.vote_list);
        replyEditText = view.findViewById(R.id.reply_edit_text);
        sendButton = view.findViewById(R.id.button_send);

        voteBar = view.findViewById(R.id.vote_header);
        voteBody = view.findViewById(R.id.vote_body);
        voteTitle = view.findViewById(R.id.vote_title);
        voteParticipant = view.findViewById(R.id.vote_participant);
        voteInfoLink = view.findViewById(R.id.vote_info_link);
        arrow = view.findViewById(R.id.arrow);
        iconVote = view.findViewById(R.id.icon_vote);
        voteStatus = view.findViewById(R.id.vote_status);
        votedView = view.findViewById(R.id.layout_voted);
        progressBarWithTimer = view.findViewById(R.id.timer);
        divider = view.findViewById(R.id.view);
        loading = view.findViewById(R.id.loading);
        main = view.findViewById(R.id.main_content);
        chatNotificationView = view.findViewById(R.id.layout_new_chat);
        chatNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToBottom();
            }
        });
        channelInfoDialog = CloseableBottomSheetDialog.createInstance(getActivity());
        channelInfoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        setupToolbar();
        prepareView();
        return view;
    }

    private void setupToolbar() {
        ViewGroup.LayoutParams params = channelBanner.getLayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
            params.height = getActivity().getResources().getDimensionPixelSize(R.dimen.channel_banner_height);
        } else {
            params.height = getActivity().getResources().getDimensionPixelSize(R.dimen
                    .channel_banner_height_without_status);
        }

        channelBanner.setLayoutParams(params);


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (getActivity() != null
                && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setHasOptionsMenu(true);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_chat_room_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            analytics.eventClickShare();
            ShareData shareData = ShareData.Builder.aShareData()
                    .setName(viewModel.getChannelName())
                    .setDescription(String.format(getString(R.string.lets_join_channel),
                            viewModel.getChannelName()))
                    .setImgUri(viewModel.getChannelInfoViewModel().getBannerUrl())
                    .setUri(viewModel.getChannelUrl())
                    .setType(ShareData.FEED_TYPE)
                    .build();

            if (shareLayout == null) {
                shareLayout = new ShareLayout(
                        GroupChatFragment.this,
                        callbackManager, channelUrl, toolbar.getTitle().toString(), analytics);
            }
            shareLayout.setShareModel(shareData);
            shareLayout.show();
            return true;
        } else if (item.getItemId() == R.id.action_info) {
            boolean temp = checkPollValid(viewModel.getChannelInfoViewModel().isHasPoll(), viewModel.getChannelInfoViewModel().getVoteInfoViewModel());
            channelInfoDialog.setContentView(createBottomSheetView(temp, viewModel
                    .getChannelInfoViewModel().getChannelViewModel(), false));
            channelInfoDialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void prepareView() {
        GroupChatTypeFactory groupChatTypeFactory = new GroupChatTypeFactoryImpl(this);
        adapter = GroupChatAdapter.createInstance(groupChatTypeFactory);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.space_med));
        chatRecyclerView.addItemDecoration(itemDecoration);

        VoteTypeFactory voteTypeFactory = new VoteTypeFactoryImpl(this);
        voteAdapter = VoteAdapter.createInstance(voteTypeFactory);

        chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1
                        && !adapter.isLoading()) {
                    presenter.loadPreviousMessages(mChannel, mPrevMessageListQuery);
                }

                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    resetNewMessageCounter();
                }

                collapse(voteBody);
                arrow.setRotation(0f);
            }
        });

        String hintText = getString(R.string.chat_as) + " " + userSession.getName() + "...";
        replyEditText.setHint(hintText);

        voteBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voteBody.getVisibility() == View.VISIBLE) {
                    collapse(voteBody);
                    arrow.setRotation(0f);
                } else {
                    KeyboardHandler.DropKeyboard(getActivity(), getView());
                    expand(voteBody);
                    analytics.eventClickVoteExpand();
                    voteAdapter.notifyDataSetChanged();
                    arrow.setRotation(180f);
                }
            }
        });

        replyTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    setSendButtonEnabled(true);
                } else {
                    setSendButtonEnabled(false);
                }
            }
        };
    }

    private void setSendButtonEnabled(boolean isEnabled) {
        if (isEnabled) {
            replyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    collapse(voteBody);
                    arrow.setRotation(0f);
                }
            });

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(replyEditText.getText().toString().trim())) {
                        PendingChatViewModel pendingChatViewModel = new PendingChatViewModel
                                (replyEditText.getText().toString(),
                                        userSession.getUserId(),
                                        userSession.getName(),
                                        userSession.getProfilePicture(),
                                        false);
                        adapter.addDummyReply(pendingChatViewModel);
                        presenter.sendReply(pendingChatViewModel, mChannel);
                    }
                }
            });
        } else {
            sendButton.setOnClickListener(null);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        progressBarWithTimer.setListener(this);
    }

    private View createBottomSheetView(boolean hasValidPoll, ChannelViewModel channelViewModel, boolean showActionButton) {
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

        if(showActionButton)
            actionButton.setVisibility(View.VISIBLE);
        else
            actionButton.setVisibility(View.GONE);

        participant.setText(TextFormatter.format(String.valueOf(channelViewModel.getParticipant())));
        name.setText(channelViewModel.getAdminName());
        title.setText(channelViewModel.getTitle());
        subtitle.setText(channelViewModel.getDescription());

        ImageHandler.loadImage2(image, channelViewModel.getImage(), R.drawable.loading_page);
        ImageHandler.loadImageCircle2(profile.getContext(), profile, channelViewModel.getAdminPicture(), R
                .drawable.loading_page);

        return view;
    }

    private View createBottomSheetView(boolean hasValidPoll, final ChannelViewModel channelViewModel) {
        return createBottomSheetView(hasValidPoll, channelViewModel, true);
    }

    private void setToolbarParticipantCount() {
        String textParticipant = String.format("%s %s", viewModel.getTotalParticipant()
                , getActivity().getString(R.string.view));
        toolbar.setSubtitle(textParticipant);
    }

    private void initData() {
        setSendButtonEnabled(false);
        presenter.getChannelInfo(viewModel.getChannelUuid());
        showLoading();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_VIEW_MODEL, viewModel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.getChannelInfoViewModel() != null
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel() != null
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel().getStartTime() != 0
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel().getEndTime() != 0
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel().getStartTime()
                < viewModel.getChannelInfoViewModel().getVoteInfoViewModel().getEndTime()
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel().getEndTime()
                > System.currentTimeMillis() / 1000L
                ) {
            progressBarWithTimer.restart();
        }

        ConnectionManager.addConnectionManagementHandler(userSession.getUserId(), ConnectionManager
                .CONNECTION_HANDLER_ID, new
                ConnectionManager.ConnectionManagementHandler() {
                    @Override
                    public void onConnected(boolean reconnect) {
                        Log.d("NISNIS", "onConnected " + reconnect);

                        if (reconnect) {
                            presenter.refreshDataAfterReconnect(mChannel);
                        }
                    }
                });

        if (viewModel != null && !TextUtils.isEmpty(viewModel.getChannelUrl()))
            presenter.setHandler(viewModel.getChannelUrl(), this);

        kickIfIdleForTooLong();
    }

    private void kickIfIdleForTooLong() {
        if (viewModel != null) {
            if (viewModel.getTimeStampBeforePause() > 0
                    && System.currentTimeMillis() - viewModel.getTimeStampBeforePause() > KICK_TRESHOLD_TIME) {
                onUserIdleTooLong();
            }
        }
    }

    @Override
    public void onPause() {
        ConnectionManager.removeConnectionManagementHandler(ConnectionManager.CONNECTION_HANDLER_ID);
        SendBird.removeChannelHandler(ConnectionManager.CHANNEL_HANDLER_ID);
        progressBarWithTimer.cancel();
        if (viewModel != null) {
            viewModel.setTimeStampBeforePause(System.currentTimeMillis());
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        presenter.logoutChannel(mChannel);
        ConnectionManager.removeConnectionManagementHandler(ConnectionManager.CONNECTION_HANDLER_ID);
        SendBird.removeChannelHandler(ConnectionManager.CHANNEL_HANDLER_ID);
        progressBarWithTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onSuccessGetPreviousMessage(List<Visitable> listChat) {
        try {
            adapter.addListPrevious(listChat);
            adapter.setCanLoadMore(mPrevMessageListQuery.hasMore());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessGetMessageFirstTime(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery) {
        try {
            this.mPrevMessageListQuery = previousMessageListQuery;
            adapter.setList(listChat);
            if (!listChat.isEmpty()) {
                adapter.setCursor(listChat.get(0));
            }
            adapter.setCanLoadMore(mPrevMessageListQuery.hasMore());
            setSendButtonEnabled(true);
            replyEditText.addTextChangedListener(replyTextWatcher);
            scrollToBottom();

            presenter.setHandler(viewModel.getChannelUrl(), this);

            if (getArguments() != null & getArguments().getBoolean(GroupChatActivity
                    .EXTRA_SHOW_BOTTOM_DIALOG, false)) {
                channelInfoDialog.show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void scrollToBottom() {
        resetNewMessageCounter();
        layoutManager.scrollToPosition(0);
    }

    private void resetNewMessageCounter() {
        newMessageCounter = 0;
        chatNotificationView.setVisibility(View.GONE);

    }

    @Override
    public void onErrorSendMessage(PendingChatViewModel pendingChatViewModel, String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        adapter.setRetry(pendingChatViewModel);
    }

    @Override
    public void onSuccessSendMessage(PendingChatViewModel pendingChatViewModel, ChatViewModel viewModel) {
        adapter.removeDummy(pendingChatViewModel);
        adapter.addReply(viewModel);
        adapter.notifyDataSetChanged();
        replyEditText.setText("");
        scrollToBottom();

    }

    @Override
    public void onErrorGetMessage(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onErrorGetMessageFirstTime(String errorMessage) {
        hideVoteLayout();
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                initData();
            }
        });
    }

    @Override
    public void onErrorGetChannelInfo(String errorMessage) {
        hideVoteLayout();
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                initData();
            }
        });
        setVisibilityHeader(View.GONE);
    }

    @Override
    public void onSuccessGetChannelInfo(ChannelInfoViewModel channelInfoViewModel) {

        hideLoading();
        this.viewModel.setChannelInfo(channelInfoViewModel);
        toolbar.setTitle(channelInfoViewModel.getTitle());
        setToolbarParticipantCount();
        ImageHandler.loadImageBlur(getActivity(), channelBanner, channelInfoViewModel
                .getBannerUrl());
        presenter.enterChannel(userSession.getUserId(), viewModel.getChannelUrl(),
                userSession.getName(), userSession.getProfilePicture(), this);
        setVisibilityHeader(View.VISIBLE);
        setVote(channelInfoViewModel.isHasPoll(), channelInfoViewModel.getVoteInfoViewModel());
        channelUrl = channelInfoViewModel.getChannelUrl();
        channelInfoDialog.setContentView(createBottomSheetView(checkPollValid(channelInfoViewModel.isHasPoll(), channelInfoViewModel.getVoteInfoViewModel()), channelInfoViewModel.getChannelViewModel()));
    }

    void setVisibilityHeader(int visible) {
        voteBar.setVisibility(visible);
        toolbar.setVisibility(visible);
        divider.setVisibility(visible);
        channelBanner.setVisibility(visible);
    }

    private void setVote(boolean hasPoll, VoteInfoViewModel voteInfoViewModel) {

        if (MethodChecker.isTimezoneNotAutomatic(getActivity())) {
            Snackbar snackBar = SnackbarManager.make(getActivity(), getString(R.string
                            .please_check_timezone_to_vote),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.action_check, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                        }
                    });
            snackBar.show();
        } else if (checkPollValid(hasPoll, voteInfoViewModel)) {
            showVoteLayout(voteInfoViewModel, "");
        } else {
            hideVoteLayout();
        }
    }

    private boolean checkPollValid(boolean hasPoll, VoteInfoViewModel voteInfoViewModel) {
        return (hasPoll
                && voteInfoViewModel != null
                && voteInfoViewModel.getStartTime() != 0
                && voteInfoViewModel.getEndTime() != 0
                && voteInfoViewModel.getStartTime() < voteInfoViewModel.getEndTime());
    }

    @Override
    public void showLoadingPreviousList() {
        adapter.showLoadingPrevious();
    }

    @Override
    public void dismissLoadingPreviousList() {
        adapter.dismissLoadingPrevious();
    }

    @Override
    public void showReconnectingMessage() {
//        NetworkErrorHelper.showSnackbar(getActivity(), "Reconnecting...");
    }

    @Override
    public void dismissReconnectingMessage() {
//        NetworkErrorHelper.showSnackbar(getActivity(), "Connected!");

    }

    @Override
    public void onSuccessRefreshReconnect(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery) {
        adapter.replaceData(listChat);
        this.mPrevMessageListQuery = previousMessageListQuery;
        adapter.setCanLoadMore(mPrevMessageListQuery.hasMore());
        scrollToBottom();
    }

    @Override
    public void onVoteOptionClicked(VoteViewModel element) {

        if (viewModel.getChannelInfoViewModel().getVoteInfoViewModel().getStatusId() == VoteInfoViewModel.STATUS_ACTIVE
                || viewModel.getChannelInfoViewModel().getVoteInfoViewModel().getStatusId() == VoteInfoViewModel.STATUS_FORCE_ACTIVE) {
            boolean voted = (votedView.getVisibility() == View.VISIBLE);
            presenter.sendVote(viewModel.getPollId(), voted, element);
            analytics.eventClickVote(element.getType(), toolbar.getTitle().toString());
        }
    }

    @Override
    public void showHasVoted() {
        View view = getLayoutInflater().inflate(R.layout.has_voted_bottom_sheet_dialog, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(R.string.has_voted);
        channelInfoDialog.setContentView(view);
        channelInfoDialog.show();
    }

    @Override
    public void showSuccessVoted() {
        View view = getLayoutInflater().inflate(R.layout.has_voted_bottom_sheet_dialog, null);
        channelInfoDialog.setContentView(view);
        channelInfoDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                channelInfoDialog.show();
            }
        },DELAY_TIME);
    }

    @Override
    public void onSuccessVote(VoteViewModel element, VoteStatisticViewModel voteStatisticViewModel) {
        if (viewModel != null
                && viewModel.getChannelInfoViewModel() != null
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel() != null) {
            voteAdapter.change(viewModel, element, voteStatisticViewModel);
            viewModel.getChannelInfoViewModel().getVoteInfoViewModel().setVoted(true);
            viewModel.getChannelInfoViewModel().getVoteInfoViewModel().setParticipant(
                    String.valueOf(Integer.parseInt(voteStatisticViewModel.getTotalParticipants())));
            setVoted();

            setVoteParticipant(viewModel.getChannelInfoViewModel()
                    .getVoteInfoViewModel().getParticipant());

        }
    }

    @Override
    public void onErrorVote(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onMessageReceived(Visitable messageItem) {
        if (messageItem instanceof VoteAnnouncementViewModel) {
            handleVoteAnnouncement((VoteAnnouncementViewModel) messageItem);
        } else {
            addIncomingMessage(messageItem);
        }
    }

    private void handleVoteAnnouncement(VoteAnnouncementViewModel messageItem) {

        switch (messageItem.getVoteType()) {
            case VoteAnnouncementViewModel.POLLING_START:
                votedView.setVisibility(View.GONE);
                showVoteLayout(messageItem.getVoteInfoViewModel(), messageItem.getVoteType());
                addIncomingMessage(messageItem);
                break;
            case VoteAnnouncementViewModel.POLLING_UPDATE:
                showVoteLayout(messageItem.getVoteInfoViewModel(), messageItem.getVoteType());
                break;
            case VoteAnnouncementViewModel.POLLING_FINISHED:
                showVoteLayout(messageItem.getVoteInfoViewModel(), messageItem.getVoteType());
                addIncomingMessage(messageItem);
                break;
            case VoteAnnouncementViewModel.POLLING_CANCEL:
                hideVoteLayout();
                break;
        }
    }

    private void addIncomingMessage(Visitable messageItem) {
        adapter.addIncomingMessage(messageItem);
        adapter.notifyItemInserted(0);
        scrollToBottomWhenPossible();
    }

    private void scrollToBottomWhenPossible() {
        if (layoutManager.findFirstVisibleItemPosition() == 0) {
            scrollToBottom();
        } else {
            newMessageCounter += 1;
            showNewMessageReceived(newMessageCounter);
        }
    }

    private void showNewMessageReceived(int newMessageCounter) {
        chatNotificationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMessageDeleted(long msgId) {
        //TODO : Implement this later
    }

    @Override
    public void onMessageUpdated(Visitable map) {
        //TODO : Implement this later
    }

    public void expand(final View v) {
        v.setVisibility(View.VISIBLE);
    }

    public void collapse(final View v) {
        v.setVisibility(View.GONE);
    }

    @Override
    public void onUserEntered(UserActionViewModel userActionViewModel, String participantCount) {

//        viewModel.setTotalParticipant(participantCount);
        viewModel.setTotalParticipant(viewModel.getTotalParticipant() + 1);
        setToolbarParticipantCount();
        adapter.addAction(userActionViewModel);
        adapter.notifyItemInserted(0);
        scrollToBottomWhenPossible();
    }

    @Override
    public void onUserExited(UserActionViewModel userActionViewModel, String participantCount) {
        try {
//            viewModel.setTotalParticipant(participantCount);
            setToolbarParticipantCount();
//        adapter.addAction(userActionViewModel);
//        adapter.notifyItemInserted(0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessEnterChannel(OpenChannel openChannel) {
        try {
            mChannel = openChannel;
            viewModel.setTotalParticipant(String.valueOf(openChannel.getParticipantCount()));
            setToolbarParticipantCount();
            presenter.initMessageFirstTime(viewModel.getChannelUuid(), mChannel);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorEnterChannel(String errorMessage) {
        if (getActivity() != null
                && getView() != null) {
            hideVoteLayout();
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.enterChannel(userSession.getUserId(), viewModel.getChannelUuid(),
                            userSession.getName(), userSession.getProfilePicture(),
                            GroupChatFragment.this);
                }
            });
        }
    }

    private void onUserIdleTooLong() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.you_have_been_kicked);
        builder.setMessage(R.string.you_have_been_idle_for_too_long);
        builder.setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getActivity().setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL);
                getActivity().finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onUserBanned(final String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.default_banned_title);
        builder.setMessage(errorMessage);
        builder.setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getActivity().setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL);
                getActivity().finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public void onChannelNotFound(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.channel_not_found);
        builder.setMessage(errorMessage);
        builder.setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getActivity().setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL);
                getActivity().finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
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

    public void showVoteLayout(final VoteInfoViewModel voteInfoViewModel, String voteType) {
        Log.d("NISNIS", "showVoteLayout");

        updateVoteViewModel(voteInfoViewModel, voteType);

        voteBar.setVisibility(View.VISIBLE);

        LinearLayoutManager voteLayoutManager;
        RecyclerView.ItemDecoration itemDecoration = null;
        if (voteInfoViewModel.getVoteOptionType().equals(VoteViewModel.IMAGE_TYPE)) {
            voteLayoutManager = new GridLayoutManager(getActivity(), 2);
            itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.space_mini), 2);
        } else {
            voteLayoutManager = new LinearLayoutManager(getActivity());
            itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.space_between), false);
        }
        voteRecyclerView.addItemDecoration(itemDecoration);
        voteRecyclerView.setLayoutManager(voteLayoutManager);
        voteRecyclerView.setAdapter(voteAdapter);
        voteTitle.setText(viewModel.getChannelInfoViewModel().getVoteInfoViewModel().getTitle());

        voteAdapter.addList(viewModel.getChannelInfoViewModel().getVoteInfoViewModel()
                .getListOption());

        if (viewModel.getChannelInfoViewModel().getVoteInfoViewModel().isVoted()) {
            setVoted();
        }

        setVoteParticipant(voteInfoViewModel.getParticipant());

        voteInfoLink.setText(voteInfoViewModel.getVoteInfoStringResId());
        voteInfoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StreamModuleRouter) getActivity().getApplicationContext()).openRedirectUrl
                        (getActivity(), voteInfoViewModel.getVoteInfoUrl());
            }
        });

        if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FINISH
                || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_FINISH) {
            progressBarWithTimer.setVisibility(View.GONE);
            progressBarWithTimer.cancel();
            setVoteHasEnded();
        } else if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_CANCELED) {
            hideVoteLayout();
        } else {
            progressBarWithTimer.setVisibility(View.VISIBLE);
            progressBarWithTimer.cancel();
            progressBarWithTimer.setTimer(voteInfoViewModel.getStartTime(), voteInfoViewModel.getEndTime());
        }

    }

    private void setVoteParticipant(String participant) {
        voteParticipant.setText(String.format("%s %s", TextFormatter.format(participant)
                , getActivity().getString(R.string.voter)));
        voteParticipant.setVisibility(View.GONE);
    }

    private void updateVoteViewModel(VoteInfoViewModel voteInfoViewModel, String voteType) {
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

    public void hideVoteLayout() {
        voteBar.setVisibility(View.GONE);
        voteBody.setVisibility(View.GONE);
    }

    public void setVoteHasEnded() {
        if (getActivity() != null) {
            progressBarWithTimer.setVisibility(View.GONE);
            voteStatus.setText(R.string.vote_has_ended);
            voteStatus.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ImageHandler.loadImageWithIdWithoutPlaceholder(iconVote, R.drawable.ic_vote_inactive);
            } else {
                iconVote.setImageResource(R.drawable.ic_vote_inactive);
            }
            voteAdapter.updateStatistic();
        }
    }

    public void setVoteStarted() {
        if (getActivity() != null) {
            progressBarWithTimer.setVisibility(View.VISIBLE);
            voteStatus.setText(R.string.vote);
            voteStatus.setTextColor(MethodChecker.getColor(getActivity(), R.color.medium_green));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ImageHandler.loadImageWithIdWithoutPlaceholder(iconVote, R.drawable.ic_vote);
            } else {
                iconVote.setImageResource(R.drawable.ic_vote);
            }
        }
    }

    public void setVoted() {
        votedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartTick() {
        setVoteStarted();
    }

    @Override
    public void onFinishTick() {
        setVoteHasEnded();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN && resultCode == Activity.RESULT_CANCELED) {
            Intent intent = ((StreamModuleRouter) getActivity().getApplicationContext())
                    .getHomeIntent(getActivity());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        } else if (requestCode == REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.removeEmptyState(getView());
            initData();
            String hintText = getString(R.string.chat_as) + " " + userSession.getName() + "...";
            replyEditText.setHint(hintText);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
    public void onRedirectUrl(String url) {
        analytics.eventClickThumbnail(url);
        if(!TextUtils.isEmpty(url)) {
            ((StreamModuleRouter) getActivity().getApplication()).openRedirectUrl(getActivity(), url);
        }
    }

    @Override
    public void onVoteComponentClicked(String type, String name) {
        if (viewModel != null
                && viewModel.getChannelInfoViewModel() != null
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel() != null
                && viewModel.getChannelInfoViewModel().isHasPoll()
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel().getStatusId() != VoteInfoViewModel.STATUS_CANCELED) {
            analytics.eventClickVoteComponent(type, name);
            if (voteBody.getVisibility() == View.GONE) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                expand(voteBody);
                arrow.setRotation(180f);
            }
        }
    }
}
