package com.tokopedia.inbox.inboxchat.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.InboxChatConstant;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity;
import com.tokopedia.inbox.inboxchat.activity.TimeMachineActivity;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.analytics.TopChatTrackingEventLabel;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomPresenter;
import com.tokopedia.inbox.inboxchat.util.Events;
import com.tokopedia.inbox.inboxchat.viewholder.ListChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;

import org.json.JSONException;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity.PARAM_SENDER_ROLE;

/**
 * Created by stevenfredian on 9/19/17.
 */

public class ChatRoomFragment extends BaseDaggerFragment
        implements ChatRoomContract.View, InboxMessageConstant, InboxChatConstant
        , WebSocketInterface {

    @Inject
    ChatRoomPresenter presenter;

    @Inject
    SessionHandler sessionHandler;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View replyView;

    private ImageView avatar;
    private TextView user;
    private TextView onlineDesc;
    private TextView label;

    ChatRoomAdapter adapter;
    private ChatRoomTypeFactory typeFactory;
    private LinearLayoutManager layoutManager;
    private ImageView sendButton;
    private EditText replyColumn;
    private ImageView attachButton;
    private View mainHeader;
    private Toolbar toolbar;
    private Observable<String> replyWatcher;
    private Observable<Boolean> replyIsTyping;
    private int mode;
    private View notifier;

    public static ChatRoomFragment createInstance(Bundle extras) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initVar();
        toolbar = (Toolbar) getActivity().findViewById(R.id.app_bar);
        mainHeader = toolbar.findViewById(R.id.main_header);
        mainHeader.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.reply_list);
        replyView = rootView.findViewById(R.id.add_comment_area);
        sendButton = (ImageView) rootView.findViewById(R.id.send_but);
        replyColumn = (EditText) rootView.findViewById(R.id.new_comment);
        sendButton.requestFocus();
        attachButton = (ImageView) rootView.findViewById(R.id.add_url);
        notifier = rootView.findViewById(R.id.notifier);
        replyWatcher = Events.text(replyColumn);
        recyclerView.setHasFixedSize(true);
        presenter.attachView(this);
        initListener();
        return rootView;
    }


    private void initListener() {

        sendButton.setOnClickListener(getSendWithWebSocketListener());

        replyIsTyping = replyWatcher.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.length() > 0;
            }
        });

        replyIsTyping.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    try {
                        presenter.setIsTyping(getArguments().getString(ChatRoomActivity
                                .PARAM_MESSAGE_ID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        replyIsTyping.debounce(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        try {
                            if (aBoolean) {
                                presenter.stopTyping(getArguments().getString(ChatRoomActivity
                                        .PARAM_MESSAGE_ID));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventInsertAttachment(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                        TopChatTrackingEventLabel.Action.CHAT_DETAIL_INSERT,
                        TopChatTrackingEventLabel.Name.CHAT_DETAIL);
                presenter.getAttachProductDialog(
                        getArguments().getString(ChatRoomActivity
                                .PARAM_SENDER_ID, ""),
                        getArguments().getString(PARAM_SENDER_ROLE, "")
                );
            }
        });
    }

    @Override
    public void addUrlToReply(String url) {
        UnifyTracking.eventSendAttachment(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                TopChatTrackingEventLabel.Action.CHAT_DETAIL_ATTACHMENT,
                TopChatTrackingEventLabel.Name.CHAT_DETAIL);
        int temp = replyColumn.getSelectionEnd();
        replyColumn.setText(replyColumn.getText() + "\n" + url);
        replyColumn.setSelection(temp);
    }

    private void initVar() {
        typeFactory = new ChatRoomTypeFactoryImpl(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        adapter = new ChatRoomAdapter(typeFactory);
        adapter.setNav(getArguments().getString(PARAM_NAV, ""));
        mode = getArguments().getInt(PARAM_MODE, InboxChatViewModel.GET_CHAT_MODE);

        presenter.getReply(mode);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int index = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (adapter.checkLoadMore(index)) {
                    presenter.onLoadMore();
                }
            }
        });

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = recyclerView.getRootView().getHeight() - recyclerView.getHeight();
                if (heightDiff > dpToPx(getActivity(), 200)) { // if more than 200 dp, it's probably a keyboard...
//                    recyclerView.smoothScrollToPosition(0);
                }
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerAppComponent daggerAppComponent = (DaggerAppComponent) DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .build();
        DaggerInboxChatComponent daggerInboxChatComponent =
                (DaggerInboxChatComponent) DaggerInboxChatComponent.builder()
                        .appComponent(daggerAppComponent).build();
        daggerInboxChatComponent.inject(this);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void finishLoading() {
//        refreshHandler.finishRefresh();
    }

    @Override
    public void setHeader() {

        if (toolbar != null) {
            mainHeader.setVisibility(View.VISIBLE);
            avatar = (ImageView) toolbar.findViewById(R.id.user_avatar);
            user = (TextView) toolbar.findViewById(R.id.title);
            onlineDesc = (TextView) toolbar.findViewById(R.id.subtitle);
            label = (TextView) toolbar.findViewById(R.id.label);
            ImageHandler.loadImageCircle2(getActivity(), avatar, getArguments().getString(PARAM_SENDER_IMAGE), R.drawable.ic_image_avatar_boy);
            user.setText(getArguments().getString(PARAM_SENDER_NAME));
            if(!getArguments().getString(PARAM_SENDER_TAG).equals(ListChatViewHolder.USER)){
                label.setText(getArguments().getString(PARAM_SENDER_TAG));
                label.setVisibility(View.VISIBLE);
            }else {
                label.setVisibility(View.GONE);
            }
            setOnlineDesc(getString(R.string.just_now));
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onGoToDetail(getArguments().getString(PARAM_SENDER_ID), getArguments().getString(PARAM_SENDER_ROLE));
                }
            });
        }
    }

    @Override
    public void setTextAreaReply(boolean textAreaReply) {
        if (textAreaReply) {
            replyView.setVisibility(View.VISIBLE);
        } else {
            replyView.setVisibility(View.GONE);
        }
    }

    @Override
    public ChatRoomAdapter getAdapter() {
        return adapter;
    }

    public void setOnlineDesc(final String when) {
        if (getActivity() != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (onlineDesc != null)
                        onlineDesc.setText(when);
                }
            });
        }
    }

    @Override
    public WebSocketInterface getInterface() {
        return this;
    }

    @Override
    public void onGoToTimeMachine(String url) {
        startActivity(TimeMachineActivity.getCallingIntent(getActivity(), url));
    }

    @Override
    public void addTimeMachine() {
        adapter.showTimeMachine();
    }

    @Override
    public String getKeyword() {
        return getArguments().getString(PARAM_KEYWORD);
    }

    @Override
    public void setResult(final ChatRoomViewModel model) {
        if (getActivity() != null
                && presenter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    presenter.setResult(model);
                }
            });
        }
    }

    @Override
    public void notifyConnectionWebSocket() {
        if (getActivity() != null && presenter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifier.setVisibility(View.VISIBLE);
                    TextView title = (TextView) notifier.findViewById(R.id.title);
                    View action = notifier.findViewById(R.id.action);
                    title.setText(R.string.error_no_connection_retrying);
                    action.setVisibility(View.VISIBLE);
                    action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notifier.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }


    @Override
    public void scrollToBottom() {
        layoutManager.scrollToPosition(adapter.getList().size() - 1);
    }

    @Override
    public String getReplyMessage() {
        return replyColumn.getText().toString();
    }

    @Override
    public void showError(String error) {
        if (adapter.getItemCount() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    mode = getArguments().getInt(PARAM_MODE, InboxChatViewModel.GET_CHAT_MODE);
                    presenter.getReply(mode);
                }
            });
        } else {
            if (error.equals("")) {
                NetworkErrorHelper.showSnackbar(getActivity());
            } else {
                NetworkErrorHelper.showSnackbar(getActivity(), error);

            }
        }
    }

    public void onSuccessSendReply(final WebSocketResponse response) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setViewEnabled(true);
                    if (isCurrentThread(response.getData().getMsgId())
                            && isMyMessage(response.getData().getFromUid())) {
                        adapter.removeLast();
                        MyChatViewModel item = new MyChatViewModel();
                        item.setReplyId(response.getData().getMsgId());
                        item.setMsgId(response.getData().getMsgId());
                        item.setSenderId(String.valueOf(response.getData().getFromUid()));
                        item.setMsg(response.getData().getMessage().getCensoredReply());
                        item.setReplyTime(response.getData().getMessage().getTimeStampUnix());
                        adapter.addReply(item);
                        finishLoading();
                        replyColumn.setText("");
                        scrollToBottom();
                    } else if (isCurrentThread(response.getData().getMsgId())) {
                        OppositeChatViewModel item = new OppositeChatViewModel();
                        item.setReplyId(response.getData().getMsgId());
                        item.setMsgId(response.getData().getMsgId());
                        item.setSenderId(String.valueOf(response.getData().getFromUid()));
                        item.setMsg(response.getData().getMessage().getCensoredReply());
                        item.setReplyTime(response.getData().getMessage().getTimeStampUnix());
                        if (adapter.isTyping()) {
                            adapter.removeTyping();
                        }
                        adapter.addReply(item);
                        finishLoading();
                        replyColumn.setText("");
                        scrollToBottom();
                    }
                    setResult();
                }
            });
        }
    }

    @Override
    public void onSuccessSendReply(ReplyActionData replyData, String reply) {
        adapter.removeLast();
        setViewEnabled(true);
        MyChatViewModel item = new MyChatViewModel();
        item.setReplyId(replyData.getChat().getMsgId());
        item.setSenderId(replyData.getChat().getSenderId());
        item.setMsg(replyData.getChat().getMsg());
        item.setReplyTime(replyData.getChat().getReplyTime());

        adapter.addReply(item);
        finishLoading();
        replyColumn.setText("");
        scrollToBottom();

        setResult();
    }

    private boolean isMyMessage(int fromUid) {
        return String.valueOf(fromUid).equals(sessionHandler.getLoginID(MainApplication.getAppContext()));
    }

    private boolean isCurrentThread(int msgId) {
        return getArguments().getString(PARAM_MESSAGE_ID).equals(String.valueOf(msgId));
    }

    @Override
    public void setViewEnabled(boolean isEnabled) {
//        replyColumn.setEnabled(isEnabled);
//        attachButton.setEnabled(isEnabled);
//        sendButton.setEnabled(isEnabled);
//        recyclerView.setEnabled(isEnabled);
//        if (isEnabled) {
////            header.setVisibility(View.VISIBLE);
//            replyView.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public void addDummyMessage() {
        MyChatViewModel item = new MyChatViewModel();
        item.setMsg(getReplyMessage());
        item.setMsgId(Integer.parseInt(getArguments().getString(InboxMessageConstant.PARAM_MESSAGE_ID)));
        item.setReplyTime(MyChatViewModel.SENDING_TEXT);
        item.setDummy(true);
        item.setSenderId(getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID));
        adapter.addReply(item);
        scrollToBottom();
    }

    private void setResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCEL, adapter.getLastItem());
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void setCanLoadMore(boolean hasNext) {
        if (hasNext) {
            adapter.showLoading();
        } else {
            adapter.removeLoading();
        }
    }

    public void restackList(Bundle data) {

        if (toolbar != null) {
            mainHeader.setVisibility(View.VISIBLE);
            avatar = (ImageView) toolbar.findViewById(R.id.user_avatar);
            user = (TextView) toolbar.findViewById(R.id.title);
            onlineDesc = (TextView) toolbar.findViewById(R.id.subtitle);
            label = (TextView) toolbar.findViewById(R.id.label);
            ImageHandler.loadImageCircle2(getActivity(), avatar, null, R.drawable.ic_image_avatar_boy);
            user.setText(getArguments().getString(PARAM_SENDER_NAME));
            label.setText(getArguments().getString(PARAM_SENDER_TAG));
            setOnlineDesc(getString(R.string.just_now));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }


    @Override
    public void hideMainLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onIncomingEvent(WebSocketResponse response) {
        switch (response.getCode()) {
            case ChatWebSocketConstant.EVENT_TOPCHAT_TYPING:
                if (String.valueOf(response.getData().getMsgId()).equals(getArguments().getString
                        (PARAM_MESSAGE_ID))) {
                    setOnlineDesc(getString(R.string.is_typing));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.showTyping();
                            if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getList().size() - 2) {
                                layoutManager.scrollToPosition(adapter.getList().size() - 1);
                            }
                        }
                    });

                }
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING:
                if (String.valueOf(response.getData().getMsgId()).equals(getArguments().getString
                        (PARAM_MESSAGE_ID))) {
                    setOnlineDesc(getString(R.string.just_now));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter.isTyping()) {
                                adapter.removeTyping();
                                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getList().size() - 2) {
                                    layoutManager.scrollToPosition(adapter.getList().size());
                                }
                            }
                        }
                    });
                }
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_READ_MESSAGE:
                adapter.setReadStatus(response);
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE:
                onSuccessSendReply(response);
                break;
            default:
                break;
        }

    }

    @Override
    public void onErrorWebSocket() {
        if (getActivity() != null && presenter != null) {
            sendButton.setOnClickListener(getSendWithApiListener());
//            notifyConnectionWebSocket();
//            presenter.createWebSocket();
        }
    }

    @Override
    public void onOpenWebSocket() {
        if (getActivity() != null && presenter != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView title = (TextView) notifier.findViewById(R.id.title);
                    title.setText(R.string.connected_websocket);
                    TextView action = (TextView) notifier.findViewById(R.id.action);
                    action.setVisibility(View.GONE);

                    sendButton.setOnClickListener(getSendWithWebSocketListener());

                }
            });
            notifier.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifier.setVisibility(View.GONE);
                }
            }, 1500);
            presenter.onOpenWebSocket();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            presenter.stopTyping(getArguments().getString(ChatRoomActivity
                    .PARAM_MESSAGE_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        presenter.closeWebSocket();
    }

    public View.OnClickListener getSendWithApiListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessageWithApi();
                UnifyTracking.sendChat(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                        TopChatTrackingEventLabel.Action.CHAT_DETAIL_SEND,
                        TopChatTrackingEventLabel.Name.CHAT_DETAIL);
            }
        };
    }

    public View.OnClickListener getSendWithWebSocketListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessageWithWebsocket();
                UnifyTracking.sendChat(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                        TopChatTrackingEventLabel.Action.CHAT_DETAIL_SEND,
                        TopChatTrackingEventLabel.Name.CHAT_DETAIL);
            }
        };
    }
}
