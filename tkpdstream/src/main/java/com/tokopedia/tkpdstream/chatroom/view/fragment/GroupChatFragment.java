package com.tokopedia.tkpdstream.chatroom.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
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
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.channel.data.analytics.ChannelAnalytics;
import com.tokopedia.tkpdstream.channel.view.ProgressBarWithTimer;
import com.tokopedia.tkpdstream.channel.view.activity.ChannelActivity;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.tkpdstream.chatroom.domain.ConnectionManager;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.view.ItemGridDecorationDivider;
import com.tokopedia.tkpdstream.chatroom.view.ShareBottomDialog;
import com.tokopedia.tkpdstream.chatroom.view.ShareData;
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
import com.tokopedia.tkpdstream.common.design.CloseableBottomSheetDialog;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.vote.view.adapter.VoteAdapter;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactory;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactoryImpl;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatFragment extends BaseDaggerFragment implements GroupChatContract.View,
        ChannelHandlerUseCase.ChannelHandlerListener, LoginGroupChatUseCase.LoginGroupChatListener, ProgressBarWithTimer.Listener {

    public static final String ARGS_VIEW_MODEL = "GC_VIEW_MODEL";

    @Inject
    GroupChatPresenter presenter;

    private Toolbar toolbar;
    private ImageView channelBanner;
    private RecyclerView chatRecyclerView;
    private RecyclerView voteRecyclerView;
    private EditText replyEditText;
    private View sendButton;
    private View voteBar;
    private View voteBody;
    private TextView voteTitle;
    private TextView participant;
    private TextView voteInfoLink;
    private ImageView iconVote;
    private View votedView;
    private TextView voteStatus;
    private ImageView arrow;
    private GroupChatAdapter adapter;
    private VoteAdapter voteAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager voteLayoutManager;
    private ProgressBarWithTimer progressBarWithTimer;

    private OpenChannel mChannel;
    private PreviousMessageListQuery mPrevMessageListQuery;
    private GroupChatViewModel viewModel;
    private UserSession userSession;

    private CloseableBottomSheetDialog channelInfoDialog;

    int newMessageCounter;
    private ShareBottomDialog shareBottomDialog;

    private CallbackManager callbackManager;

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
            getActivity().setResult(ChannelActivity.RESULT_ERROR_LOGIN, intent);
            getActivity().finish();
        }

        userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();

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
        participant = view.findViewById(R.id.participant);
        voteInfoLink = view.findViewById(R.id.vote_info_link);
        arrow = view.findViewById(R.id.arrow);
        iconVote = view.findViewById(R.id.icon_vote);
        voteStatus = view.findViewById(R.id.vote_status);
        votedView = view.findViewById(R.id.layout_voted);
        progressBarWithTimer = view.findViewById(R.id.timer);
        channelInfoDialog = CloseableBottomSheetDialog.createInstance(getActivity());

        setupToolbar();
        prepareView();
        return view;
    }

    private void setupToolbar() {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getActivity().getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(getResources()
//                    .getColor(R.color.grey_transparent_background));
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getActivity().getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
//        }

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
//            presenter.shareChatRoom(viewModel);
            ShareData shareData = ShareData.Builder.aShareData()
                    .setName("Judul")
                    .setDescription("Konten")
                    .setImgUri("ImageUri")
                    .setUri("URL")
                    .setType(ShareData.FEED_TYPE)
                    .build();

            if (shareBottomDialog == null) {
                shareBottomDialog = new ShareBottomDialog(
                        GroupChatFragment.this,
                        callbackManager);
            }
            shareBottomDialog.setShareModel(shareData);

            shareBottomDialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void prepareView() {
//        final Animation an = new RotateAnimation(0.0f, 360.0f, 0, 0);
//        an.setRepeatMode(Animation.REVERSE);
//        an.setFillAfter(true);
//        arrow.setAnimation(an);
        GroupChatTypeFactory groupChatTypeFactory = new GroupChatTypeFactoryImpl(this);
        adapter = GroupChatAdapter.createInstance(groupChatTypeFactory);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);

        VoteTypeFactory voteTypeFactory = new VoteTypeFactoryImpl(this);
        voteAdapter = VoteAdapter.createInstance(voteTypeFactory);

        chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1
                        && !adapter.isLoading()) {
                    presenter.loadPreviousMessages(mChannel, mPrevMessageListQuery);
                }
            }
        });

        replyEditText.addTextChangedListener(new TextWatcher() {
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
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PendingChatViewModel pendingChatViewModel = new PendingChatViewModel
                        (replyEditText.getText().toString(),
                                userSession.getUserId(),
                                userSession.getName(),
                                "https://yt3.ggpht.com/-uwClWniyyFU/AAAAAAAAAAI/AAAAAAAAAAA/nVrBEY3dzuY/s176-c-k-no-mo-rj-c0xffffff/photo.jpg",
                                false);
                adapter.addDummyReply(pendingChatViewModel);
                presenter.sendReply(pendingChatViewModel, mChannel);
            }
        });

        voteBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(voteBody.getVisibility() == View.VISIBLE){
                    collapse(voteBody);
                }else {
                    expand(voteBody);
                }
                arrow.animate().rotationBy(180f).start();
            }
        });
    }

    private void setSendButtonEnabled(boolean isEnabled) {
        sendButton.setEnabled(isEnabled);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        progressBarWithTimer.setDate(1519120063, 1519170063);
        progressBarWithTimer.setListener(this);

        toolbar.setTitle(viewModel.getChannelName());
        setToolbarParticipantCount();

        presenter.getVoteInfo(getActivity());

        ImageHandler.loadImageBlur(getActivity(), channelBanner, "http://static.tvtropes" +
                ".org/pmwiki/pub/images/kingdom_hearts_difficulties.jpg");


        ChannelViewModel model = getArguments().getParcelable(GroupChatActivity.EXTRA_CHANNEL_INFO);
        channelInfoDialog.setContentView(createBottomSheetView(model));
        channelInfoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        channelInfoDialog.show();

    }

    private View createBottomSheetView(final ChannelViewModel channelViewModel) {
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
            }
        });
        actionButton.setText(R.string.lets_vote);

        participant.setText(String.valueOf(channelViewModel.getParticipant()));
        name.setText(channelViewModel.getName());
        title.setText(channelViewModel.getTitle());
        subtitle.setText(channelViewModel.getSubtitle());

        ImageHandler.loadImage2(image, channelViewModel.getImage(), R.drawable.loading_page);
        ImageHandler.loadImageCircle2(profile.getContext(), profile, channelViewModel.getProfile(), R.drawable.loading_page);

        return view;
    }


    private void setToolbarParticipantCount() {
        String textParticipant = viewModel.getTotalParticipant() + " " + getString(R.string
                .participant);
        toolbar.setSubtitle(textParticipant);
    }

    private void initData() {
        callbackManager = CallbackManager.Factory.create();
        presenter.getChannelInfo(viewModel.getChannelUuid());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_VIEW_MODEL, viewModel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.logoutChannel(mChannel);

    }

    @Override
    public void onResume() {
        super.onResume();

        ConnectionManager.addConnectionManagementHandler(userSession.getUserId(), ConnectionManager
                .CONNECTION_HANDLER_ID, new
                ConnectionManager.ConnectionManagementHandler() {
                    @Override
                    public void onConnected(boolean reconnect) {
                        Log.d("NISNIS", "onConnected " + reconnect);

                        if (reconnect) {
//                    presenter.refreshData();
                        } else {

                        }
                    }
                });
        presenter.setHandler(viewModel.getChannelUuid(), this);
    }

    @Override
    public void onPause() {
        ConnectionManager.removeConnectionManagementHandler(ConnectionManager.CONNECTION_HANDLER_ID);
        SendBird.removeChannelHandler(ConnectionManager.CHANNEL_HANDLER_ID);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onSuccessGetMessage(List<Visitable> listChat) {
        adapter.addList(listChat);
    }

    @Override
    public void onSuccessGetMessageFirstTime(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery) {
        this.mPrevMessageListQuery = previousMessageListQuery;
        adapter.addList(listChat);
        scrollToBottom();
    }

    private void scrollToBottom() {
        resetNewMessageCounter();
        layoutManager.scrollToPosition(0);
    }

    private void resetNewMessageCounter() {
        newMessageCounter = 0;
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
    public void onErrorGetChannelInfo(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                initData();
            }
        });
    }

    @Override
    public void onSuccessGetChannelInfo(ChannelInfoViewModel channelInfoViewModel) {
        this.viewModel.setChannelUrl(channelInfoViewModel.getChannelUrl());
        presenter.enterChannel(userSession.getUserId(), viewModel.getChannelUrl(),
                userSession.getName(), "https://yt3.ggpht" +
                        ".com/-uwClWniyyFU/AAAAAAAAAAI/AAAAAAAAAAA/nVrBEY3dzuY/s176-c-k-no-mo-rj" +
                        "-c0xffffff/photo.jpg", this);
    }

    @Override
    public void showLoadingList() {
        adapter.showLoading();
    }

    @Override
    public void dismissLoadingList() {
        adapter.dismissLoading();
    }

    @Override
    public void onMessageReceived(Visitable messageItem) {
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
        Log.d("NISNIS", "showNewMessageReceived " + newMessageCounter);
    }

    @Override
    public void onMessageDeleted(long msgId) {
//TODO : Implement this later
    }

    @Override
    public void onMessageUpdated(Visitable map) {
//TODO : Implement this later
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    @Override
    public void onSuccessGetVoteInfo(final VoteInfoViewModel voteInfoViewModel) {
        if(voteInfoViewModel.getVoteType() == VoteViewModel.IMAGE_TYPE){
            voteLayoutManager = new GridLayoutManager(getActivity(), 2);
//            ItemGridDecorationDivider itemDecoration = new ItemGridDecorationDivider(2, R.dimen.space_mini, false);
//            voteRecyclerView.addItemDecoration(itemDecoration);
        }else {
            voteLayoutManager = new LinearLayoutManager(getActivity());
        }
        voteRecyclerView.setLayoutManager(voteLayoutManager);
        voteRecyclerView.setAdapter(voteAdapter);
        voteAdapter.addList(voteInfoViewModel.getList());
        voteStatus.setText(voteInfoViewModel.getVoteStatus());
        voteTitle.setText(voteInfoViewModel.getTitle());
        if(voteInfoViewModel.isVoted()){
            setVoted();
        }
        participant.setText(voteInfoViewModel.getParticipant());
        voteInfoLink.setText(voteInfoViewModel.getVoteInfoString());
        voteInfoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openwebview from getVoteInfoUrl
            }
        });
    }


    @Override
    public void onUserEntered(UserActionViewModel userActionViewModel) {

        if (!userActionViewModel.getUserId().equals(userSession.getUserId())) {
            viewModel.setTotalParticipant(viewModel.getTotalParticipant() + 1);
            setToolbarParticipantCount();
        }
        adapter.addAction(userActionViewModel);
        adapter.notifyItemInserted(0);
        scrollToBottomWhenPossible();
    }

    @Override
    public void onUserExited(UserActionViewModel userActionViewModel) {
        viewModel.setTotalParticipant(viewModel.getTotalParticipant() - 1);
        setToolbarParticipantCount();
//        adapter.addAction(userActionViewModel);
//        adapter.notifyItemInserted(0);
    }

    @Override
    public void onSuccessEnterChannel(OpenChannel openChannel) {
        mChannel = openChannel;
        viewModel.setTotalParticipant(openChannel.getParticipantCount());
        viewModel.setChannelName(mChannel.getName());
        setToolbarParticipantCount();
        setChannelName();
        presenter.initMessageFirstTime(viewModel.getChannelUuid(), mChannel);
    }

    private void setChannelName() {
        toolbar.setTitle(viewModel.getChannelName());
    }

    @Override
    public void onErrorEnterChannel(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.enterChannel(userSession.getUserId(), viewModel.getChannelUuid(),
                        userSession.getName(), "https://yt3.ggpht" +
                                ".com/-uwClWniyyFU/AAAAAAAAAAI/AAAAAAAAAAA/nVrBEY3dzuY/s176-c-k-no-mo-rj" +
                                "-c0xffffff/photo.jpg", GroupChatFragment.this);
            }
        });
    }

    public void setVoteHasEnded(){
        if(getActivity() != null) {
            voteStatus.setText(R.string.vote_has_ended);
            voteStatus.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
            DrawableCompat.setTint(iconVote.getBackground(), ContextCompat.getColor(getActivity(), R.color.black_54));
            progressBarWithTimer.setVisibility(View.GONE);
        }
    }

    public void setVoted(){
        votedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishTick() {
        setVoteHasEnded();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
