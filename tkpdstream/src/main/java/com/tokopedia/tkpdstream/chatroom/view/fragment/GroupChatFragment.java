package com.tokopedia.tkpdstream.chatroom.view.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.channel.data.analytics.ChannelAnalytics;
import com.tokopedia.tkpdstream.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.tkpdstream.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.GroupChatAdapter;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChatroomContract;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.presenter.ChatroomPresenter;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.VibrateViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.tkpdstream.common.design.CloseableBottomSheetDialog;
import com.tokopedia.tkpdstream.common.design.SpaceItemDecoration;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.util.StreamAnalytics;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatFragment extends BaseDaggerFragment implements ChatroomContract.View,
        ChatroomContract.View.ImageAnnouncementViewHolderListener,
        ChatroomContract.View.VoteAnnouncementViewHolderListener,
        ChatroomContract.View.SprintSaleViewHolderListener {

    private static final long DELAY_TIME_SPRINT_SALE = TimeUnit.SECONDS.toMillis(3);
    private static final long MILIS_TO_SECOND = 1000;

    @Inject
    ChatroomPresenter presenter;

    @Inject
    StreamAnalytics analytics;

    private RecyclerView chatRecyclerView;
    private EditText replyEditText;
    private View sendButton;
    private View divider;
    private View main, loading;
    private GroupChatAdapter adapter;
    private LinearLayoutManager layoutManager;
    private View chatNotificationView;
    private View login;
    private View sprintSaleIconLayout;
    private TextView sprintSaleText;

    private OpenChannel mChannel;
    private PreviousMessageListQuery mPrevMessageListQuery;
    private UserSession userSession;

    private CloseableBottomSheetDialog channelInfoDialog;

    private static final int REQUEST_LOGIN = 111;
    private Handler sprintSaleHandler;
    private Runnable sprintSaleRunnable;

    int newMessageCounter;

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
        userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat_room_new, container, false);
        chatRecyclerView = view.findViewById(R.id.chat_list);
        replyEditText = view.findViewById(R.id.reply_edit_text);
        sendButton = view.findViewById(R.id.button_send);
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

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        login = view.findViewById(R.id.login);
        sprintSaleIconLayout = view.findViewById(R.id.sprintsale_icon_layout);
        sprintSaleText = view.findViewById(R.id.sprintsale_text);
        prepareView();
        return view;
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

            }
        });

        setReplyTextHint();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(((StreamModuleRouter) getActivity().getApplicationContext())
                        .getLoginIntent(getActivity()), REQUEST_LOGIN);
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
        setForLoginUser(userSession != null && userSession.isLoggedIn());

        sprintSaleIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SprintSaleViewModel sprintSaleViewModel = ((GroupChatContract.View) getActivity())
                        .getSprintSaleViewModel();

                onSprintSaleClicked(sprintSaleViewModel.getRedirectUrl(), sprintSaleViewModel.getCampaignName());
            }
        });
    }

    private void setSendButtonEnabled(boolean isEnabled) {
        if (isEnabled) {

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
    }

    private void initData() {
        setSendButtonEnabled(false);
        presenter.initMessageFirstTime(mChannel);
        showLoading();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void refreshChat() {
        presenter.refreshDataAfterReconnect(mChannel);
    }

    @Override
    public void setReplyTextHint() {
        String hintText = getString(R.string.chat_as) + " " + userSession.getName() + "...";
        replyEditText.setHint(hintText);
    }

    @Override
    public void setSprintSaleIcon(SprintSaleViewModel sprintSaleViewModel) {
        if (sprintSaleViewModel != null
                && isValidSprintSale(sprintSaleViewModel)
                && sprintSaleViewModel.getSprintSaleType() != null
                && !sprintSaleViewModel.getSprintSaleType().equals(SprintSaleViewModel.TYPE_FINISHED)) {
            sprintSaleIconLayout.setVisibility(View.VISIBLE);
            setupSprintSaleIcon(sprintSaleViewModel);
        } else {
            sprintSaleIconLayout.setVisibility(View.GONE);
        }
    }

    private void setupSprintSaleIcon(SprintSaleViewModel sprintSaleViewModel) {
        long currentTime = new Date().getTime() / MILIS_TO_SECOND;
        if (currentTime < sprintSaleViewModel.getStartDate()) {
            sprintSaleText.setText(String.format("%s - %s", sprintSaleViewModel
                    .getFormattedStartDate(), sprintSaleViewModel.getFormattedEndDate()));
        } else {
            sprintSaleText.setText(getString(R.string.ongoing));
        }
    }

    @Override
    public void autoAddSprintSaleAnnouncement(@Nullable final SprintSaleViewModel
                                                      sprintSaleViewModel,
                                              @Nullable final ChannelInfoViewModel channelInfoViewModel) {
        if (sprintSaleViewModel != null
                && isValidSprintSale(sprintSaleViewModel)
                && sprintSaleViewModel.getSprintSaleType() != null
                && !sprintSaleViewModel.getSprintSaleType().equals(SprintSaleViewModel.TYPE_UPCOMING)
                && channelInfoViewModel != null) {

            sprintSaleHandler = new Handler();
            sprintSaleRunnable = new Runnable() {
                @Override
                public void run() {
                    SprintSaleAnnouncementViewModel sprintSaleAnnouncementViewModel = new SprintSaleAnnouncementViewModel(
                            new Date().getTime() / MILIS_TO_SECOND,
                            new Date().getTime() / MILIS_TO_SECOND,
                            "0",
                            "0",
                            channelInfoViewModel.getAdminName(),
                            channelInfoViewModel.getAdminPicture(),
                            false,
                            true,
                            sprintSaleViewModel.getRedirectUrl(),
                            sprintSaleViewModel.getListProduct(),
                            sprintSaleViewModel.getCampaignName(),
                            sprintSaleViewModel.getStartDate(),
                            sprintSaleViewModel.getEndDate(),
                            SprintSaleViewModel.TYPE_UPCOMING
                    );

                    if (adapter.getItemAt(adapter.getItemCount() - 1) != null
                            && !(adapter.getItemAt(adapter.getItemCount() - 1) instanceof
                            SprintSaleAnnouncementViewModel)) {
                        addIncomingMessage(sprintSaleAnnouncementViewModel);
                        ((GroupChatContract.View) getActivity()).vibratePhone();
                    }
                }
            };
            sprintSaleHandler.postDelayed(sprintSaleRunnable, DELAY_TIME_SPRINT_SALE);

        }
    }

    public void autoAddGroupChatPoints(@Nullable final GroupChatPointsViewModel
                                               groupChatPointsViewModel) {
        if (groupChatPointsViewModel != null) {
            if (adapter.getItemAt(adapter.getItemCount() - 1) != null
                    && !(adapter.getItemAt(adapter.getItemCount() - 1) instanceof GroupChatPointsViewModel)
                    && groupChatPointsViewModel != null) {
                addIncomingMessage(groupChatPointsViewModel);
                ((GroupChatContract.View) getActivity()).vibratePhone();
            }
        }
    }

    private boolean isValidSprintSale(SprintSaleViewModel sprintSaleViewModel) {
        long currentTime = new Date().getTime() / MILIS_TO_SECOND;
        return sprintSaleViewModel != null
                && sprintSaleViewModel.getStartDate() != 0
                && sprintSaleViewModel.getEndDate() != 0
                && sprintSaleViewModel.getEndDate() > currentTime;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sprintSaleHandler != null && sprintSaleRunnable != null) {
            sprintSaleHandler.removeCallbacks(sprintSaleRunnable);
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
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

            hideLoading();

            if (getActivity() instanceof GroupChatContract.View) {
                ((GroupChatContract.View) getActivity()).setChannelHandler();
                ((GroupChatContract.View) getActivity()).showInfoDialog();
                autoAddSprintSaleAnnouncement(
                        ((GroupChatContract.View) getActivity()).getSprintSaleViewModel(),
                        ((GroupChatContract.View) getActivity()).getChannelInfoViewModel());

                setSprintSaleIcon(((GroupChatContract.View) getActivity()).getSprintSaleViewModel());

            }
            autoAddGroupChatPoints(((GroupChatContract.View) getActivity()).getChannelInfoViewModel().getGroupChatPointsViewModel());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void scrollToBottom() {
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
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                initData();
            }
        });
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
        showLoading();
    }

    @Override
    public void dismissReconnectingMessage() {
//        NetworkErrorHelper.showSnackbar(getActivity(), "Connected!");
        hideLoading();
    }

    @Override
    public void onSuccessRefreshReconnect(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery) {
        adapter.replaceData(listChat);
        this.mPrevMessageListQuery = previousMessageListQuery;
        adapter.setCanLoadMore(mPrevMessageListQuery.hasMore());
        scrollToBottom();

        if (getActivity() instanceof GroupChatActivity) {
            ((GroupChatActivity) getActivity()).setChannelHandler();
            autoAddSprintSaleAnnouncement(
                    ((GroupChatContract.View) getActivity()).getSprintSaleViewModel(),
                    ((GroupChatContract.View) getActivity()).getChannelInfoViewModel());

            setSprintSaleIcon(((GroupChatContract.View) getActivity()).getSprintSaleViewModel());
        }

        setSendButtonEnabled(true);

    }

    public void onMessageReceived(Visitable messageItem) {
        if (messageItem instanceof VoteAnnouncementViewModel) {
            handleVoteAnnouncement((VoteAnnouncementViewModel) messageItem);
        } else if (messageItem instanceof SprintSaleAnnouncementViewModel) {
            setSprintSaleIcon(((GroupChatContract.View)getActivity()).getSprintSaleViewModel());
            addIncomingMessage(messageItem);
        } else if (!(messageItem instanceof
                VibrateViewModel)) {
            addIncomingMessage(messageItem);
        }

    }

    private void handleVoteAnnouncement(VoteAnnouncementViewModel messageItem) {

        switch (messageItem.getVoteType()) {
            case VoteAnnouncementViewModel.POLLING_START:
            case VoteAnnouncementViewModel.POLLING_FINISHED:
                addIncomingMessage(messageItem);
                break;
            case VoteAnnouncementViewModel.POLLING_UPDATE:
            case VoteAnnouncementViewModel.POLLING_CANCEL:
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
        if (login.getVisibility() != View.VISIBLE) {
            chatNotificationView.setVisibility(View.VISIBLE);
        }
    }

    public void onMessageDeleted(long msgId) {
        adapter.deleteMessage(msgId);
    }

    public void onMessageUpdated(Visitable visitable) {
        adapter.updateMessage(visitable);
    }

    public void onUserEntered(UserActionViewModel userActionViewModel) {
        adapter.addAction(userActionViewModel);
        adapter.notifyItemInserted(0);
        scrollToBottomWhenPossible();
    }

    public void onUserExited(UserActionViewModel userActionViewModel) {
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
    public void onImageAnnouncementClicked(String url) {
        analytics.eventClickThumbnail(url);
        if (!TextUtils.isEmpty(url)) {
            ((StreamModuleRouter) getActivity().getApplication()).openRedirectUrl(getActivity(), url);
        }
    }

    @Override
    public void onVoteComponentClicked(String type, String name) {
        if (getActivity() instanceof GroupChatActivity) {
            ((GroupChatActivity) getActivity()).moveToVoteFragment();
        }
        analytics.eventClickVoteComponent(StreamAnalytics.COMPONENT_VOTE, name);
    }

    public void setChannel(OpenChannel mChannel) {
        this.mChannel = mChannel;
    }

    private void setForLoginUser(boolean isLoggedIn) {
        if (isLoggedIn) {
            divider.setVisibility(View.VISIBLE);
            replyEditText.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.GONE);
            replyEditText.setVisibility(View.GONE);
            sendButton.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSprintSaleClicked(String url, String campaignName) {

        ((GroupChatContract.View) getActivity()).eventClickComponent(StreamAnalytics
                .COMPONENT_FLASH_SALE, campaignName, StreamAnalytics.ATTRIBUTE_FLASH_SALE);

        ((StreamModuleRouter) getActivity().getApplicationContext()).openRedirectUrl(getActivity()
                , ((GroupChatContract.View) getActivity()).generateAttributeApplink(url,
                        StreamAnalytics.ATTRIBUTE_FLASH_SALE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN
                && resultCode == Activity.RESULT_OK) {
            userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
            setForLoginUser(userSession != null && userSession.isLoggedIn());
        }
    }

    public void onPushNotifReceived(GroupChatPointsViewModel model) {
        autoAddGroupChatPoints(model);
    }
}
