package com.tokopedia.tkpdstream.chatroom.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.channel.data.analytics.ChannelAnalytics;
import com.tokopedia.tkpdstream.channel.view.ProgressBarWithTimer;
import com.tokopedia.tkpdstream.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.tkpdstream.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.GroupChatAdapter;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChatroomContract;
import com.tokopedia.tkpdstream.chatroom.view.presenter.ChatroomPresenter;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.BaseChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.tkpdstream.common.design.CloseableBottomSheetDialog;
import com.tokopedia.tkpdstream.common.design.SpaceItemDecoration;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.util.StreamAnalytics;
import com.tokopedia.tkpdstream.vote.view.adapter.VoteAdapter;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactory;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactoryImpl;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteStatisticViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatFragment extends BaseDaggerFragment implements ChatroomContract.View,
        ProgressBarWithTimer.Listener, ChatroomContract.View.ImageViewHolderListener, ChatroomContract.View.VoteAnnouncementViewHolderListener {

    public static final String ARGS_VIEW_MODEL = "GC_VIEW_MODEL";
    private static final long DELAY_TIME = 1000L;
    private static final long VIBRATE_LENGTH = 500;

    @Inject
    ChatroomPresenter presenter;

    @Inject
    StreamAnalytics analytics;

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
    //    private ImageView arrow;
    private GroupChatAdapter adapter;
    private VoteAdapter voteAdapter;
    private LinearLayoutManager layoutManager;
    private ProgressBarWithTimer progressBarWithTimer;
    private View chatNotificationView;
    private View login;

    private OpenChannel mChannel;
    private PreviousMessageListQuery mPrevMessageListQuery;
    private UserSession userSession;

    private CloseableBottomSheetDialog channelInfoDialog;

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
        voteRecyclerView = view.findViewById(R.id.vote_list);
        replyEditText = view.findViewById(R.id.reply_edit_text);
        sendButton = view.findViewById(R.id.button_send);

        voteBar = view.findViewById(R.id.vote_header);
        voteBody = view.findViewById(R.id.vote_body);
        voteTitle = view.findViewById(R.id.vote_title);
        voteParticipant = view.findViewById(R.id.vote_participant);
        voteInfoLink = view.findViewById(R.id.vote_info_link);
//        arrow = view.findViewById(R.id.arrow);
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
        login = view.findViewById(R.id.login);
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
            }
        });

        setReplyTextHint();

        voteBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voteBody.getVisibility() == View.VISIBLE) {
                    collapse(voteBody);
                } else {
                    KeyboardHandler.DropKeyboard(getActivity(), getView());
                    expand(voteBody);
                    analytics.eventClickVoteExpand();
                    voteAdapter.notifyDataSetChanged();
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

        setForLoginUser(userSession.isLoggedIn());
    }

    private void setSendButtonEnabled(boolean isEnabled) {
        if (isEnabled) {
            replyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    collapse(voteBody);
//                    arrow.setRotation(0f);
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
    public void onPause() {
        progressBarWithTimer.cancel();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
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

            hideLoading();

            if (getActivity() instanceof GroupChatActivity) {
                ((GroupChatActivity) getActivity()).setChannelHandler();
                ((GroupChatActivity) getActivity()).showInfoDialog();
            }

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
        hideVoteLayout();
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

        if (getActivity() instanceof GroupChatActivity) {
            ((GroupChatActivity) getActivity()).setChannelHandler();
        }

    }

    @Override
    public void onVoteOptionClicked(VoteViewModel element) {

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                channelInfoDialog.show();
            }
        }, DELAY_TIME);
    }

    @Override
    public void onSuccessVote(VoteViewModel element, VoteStatisticViewModel voteStatisticViewModel) {

    }

    @Override
    public void onErrorVote(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    public void onMessageReceived(Visitable messageItem) {
        if (messageItem instanceof VoteAnnouncementViewModel) {
            handleVoteAnnouncement((VoteAnnouncementViewModel) messageItem);
        } else {
            addIncomingMessage(messageItem);
        }

        if (messageItem instanceof BaseChatViewModel
                && ((BaseChatViewModel) messageItem).isAdministrator()) {
            vibratePhone();
        }
    }


    private void vibratePhone() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_LENGTH, VibrationEffect
                        .DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(VIBRATE_LENGTH);
            }
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

    public void expand(final View v) {
        v.setVisibility(View.VISIBLE);
    }

    public void collapse(final View v) {
        v.setVisibility(View.GONE);
    }

    public void onUserEntered(UserActionViewModel userActionViewModel) {
        Log.d("NISNIS", "onUserEntered " + userActionViewModel.getUserName());
        adapter.addAction(userActionViewModel);
        adapter.notifyItemInserted(0);
        scrollToBottomWhenPossible();
    }

    public void onUserExited(UserActionViewModel userActionViewModel) {
        Log.d("NISNIS", "onUserExited " + userActionViewModel.getUserName());
    }


    public void showVoteLayout(final VoteInfoViewModel voteInfoViewModel, String voteType) {
        if (getActivity() instanceof GroupChatActivity) {
            ((GroupChatActivity) getActivity()).updateVoteViewModel(voteInfoViewModel, voteType);
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

    @Override
    public void onStartTick() {
        setVoteStarted();
    }

    @Override
    public void onFinishTick() {
        setVoteHasEnded();
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
        if (!TextUtils.isEmpty(url)) {
            ((StreamModuleRouter) getActivity().getApplication()).openRedirectUrl(getActivity(), url);
        }
    }

    @Override
    public void onVoteComponentClicked(String type, String name) {

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
}
