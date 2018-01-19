package com.tokopedia.inbox.inboxchat.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.InboxChatConstant;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.activity.ChatMarketingThumbnailActivity;
import com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity;
import com.tokopedia.inbox.inboxchat.activity.SendMessageActivity;
import com.tokopedia.inbox.inboxchat.activity.TemplateChatActivity;
import com.tokopedia.inbox.inboxchat.activity.TimeMachineActivity;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.adapter.TemplateChatAdapter;
import com.tokopedia.inbox.inboxchat.adapter.TemplateChatTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.TemplateChatTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.analytics.TopChatTrackingEventLabel;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomPresenter;
import com.tokopedia.inbox.inboxchat.util.Events;
import com.tokopedia.inbox.inboxchat.viewholder.ListChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity.PARAM_SENDER_ROLE;
import static com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity.PARAM_WEBSOCKET;

/**
 * Created by stevenfredian on 9/19/17.
 */

public class ChatRoomFragment extends BaseDaggerFragment
        implements ChatRoomContract.View, InboxMessageConstant, InboxChatConstant
        , WebSocketInterface {

    private static final String ENABLE_TOPCHAT = "topchat_template";
    @Inject
    ChatRoomPresenter presenter;

    @Inject
    SessionHandler sessionHandler;

    public static final String FILELOC = "fileloc";
    private RecyclerView recyclerView;
    private RecyclerView templateRecyclerView;
    private ProgressBar progressBar;
    private View replyView;

    private ImageView avatar;
    private TextView user;
    private TextView onlineDesc;
    private TextView label;

    ChatRoomAdapter adapter;
    TemplateChatAdapter templateAdapter;

    private ChatRoomTypeFactory typeFactory;
    private TemplateChatTypeFactory templateChatFactory;
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
    private LinearLayoutManager templateLayoutManager;

    private RemoteConfig remoteConfig;

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
        toolbar = getActivity().findViewById(R.id.app_bar);
        mainHeader = toolbar.findViewById(R.id.main_header);
        mainHeader.setVisibility(View.INVISIBLE);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = rootView.findViewById(R.id.reply_list);
        templateRecyclerView = rootView.findViewById(R.id.list_template);
        replyView = rootView.findViewById(R.id.add_comment_area);
        sendButton = rootView.findViewById(R.id.send_but);
        replyColumn = rootView.findViewById(R.id.new_comment);
        sendButton.requestFocus();
        attachButton = rootView.findViewById(R.id.add_url);
        notifier = rootView.findViewById(R.id.notifier);
        replyWatcher = Events.text(replyColumn);
        recyclerView.setHasFixedSize(true);
        templateRecyclerView.setHasFixedSize(true);
        presenter.attachView(this);
        prepareView();
        initListener();
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        return rootView;
    }

    private void prepareView() {
        if (getArguments().getBoolean(SendMessageActivity.IS_HAS_ATTACH_BUTTON)) {
            attachButton.setVisibility(View.VISIBLE);
        } else {
            attachButton.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(getArguments().getString(SendMessageActivity.PARAM_CUSTOM_MESSAGE,
                ""))) {
            String customMessage = "\n" + getArguments().getString(SendMessageActivity
                    .PARAM_CUSTOM_MESSAGE, "");
            replyColumn.setText(customMessage);
        }
    }

    public boolean isAllowedTemplate(){
        return (remoteConfig.getBoolean(ENABLE_TOPCHAT));
    }


    private void initListener() {

        if (needCreateWebSocket()) {
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
                    try {
                        if (aBoolean)
                            presenter.setIsTyping(getArguments().getString(ChatRoomActivity
                                    .PARAM_MESSAGE_ID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            replyIsTyping.debounce(2, TimeUnit.SECONDS)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            try {
                                presenter.stopTyping(getArguments().getString(ChatRoomActivity
                                        .PARAM_MESSAGE_ID));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UnifyTracking.eventSendMessagePage();
                    presenter.initMessage(replyColumn.getText().toString(),
                            getArguments().getString(ChatRoomActivity.PARAM_SOURCE),
                            getArguments().getString(ChatRoomActivity.PARAM_SENDER_ID),
                            getArguments().getString(ChatRoomActivity.PARAM_USER_ID));
                }
            });

        }


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
        replyColumn.setText(String.format("%s\n%s", replyColumn.getText(), url));
        replyColumn.setSelection(temp);
    }

    @Override
    public void addTemplateString(String message) {
        String labelCategory = TopChatTrackingEventLabel.Category.INBOX_CHAT;
        if (!getArguments().getBoolean(PARAM_WEBSOCKET)) {
            if (getArguments().getString(PARAM_SENDER_TAG).equals(ChatRoomActivity.ROLE_SELLER)) {
                labelCategory = TopChatTrackingEventLabel.Category.SHOP_PAGE;
            }
            if (getArguments().getString(SendMessageActivity.PARAM_CUSTOM_MESSAGE,"").length()>0){
                labelCategory = TopChatTrackingEventLabel.Category.PRODUCT_PAGE;
            }
        }

        UnifyTracking.eventClickTemplate(labelCategory,
                TopChatTrackingEventLabel.Action.TEMPLATE_CHAT_CLICK,
                TopChatTrackingEventLabel.Name.INBOX_CHAT);
        String text =  replyColumn.getText().toString();
        int index = replyColumn.getSelectionStart();
        replyColumn.setText(String.format("%s %s %s", text.substring(0,index) , message, text.substring(index)));
        replyColumn.setSelection(message.length() + text.substring(0,index).length() +1);
    }

    @Override
    public void goToSettingTemplate() {
        Intent intent = TemplateChatActivity.createInstance(getActivity());
        startActivityForResult(intent, 100);
        getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
    }

    @Override
    public void onGoToGallery(Attachment attachment) {

        ArrayList<String> strings = new ArrayList<>();
        strings.add(attachment.getAttributes().getImageUrl());

        ((PdpRouter) getActivity().getApplication()).openImagePreview(getActivity(), strings, new ArrayList<String>(), 1);
    }

    @Override
    public void onGoToWebView(String url, String id) {
        UnifyTracking.eventClickThumbnailMarketing(TopChatTrackingEventLabel.Category.INBOX_CHAT,
                TopChatTrackingEventLabel.Action.CLICK_THUMBNAIL,
                TopChatTrackingEventLabel.Name.INBOX_CHAT,
                id
                );
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        startActivity(ChatMarketingThumbnailActivity.getCallingIntent(getActivity(), url));
    }

    @Override
    public boolean needCreateWebSocket() {
        return getArguments().getBoolean(PARAM_WEBSOCKET);
    }

    @Override
    public void hideNotifier() {
        notifier.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessInitMessage() {
        CommonUtils.UniversalToast(getActivity(), getString(R.string.success_send_msg));
        getActivity().finish();
    }


    @Override
    public void onErrorInitMessage(String s) {
        adapter.removeLast();
        NetworkErrorHelper.showSnackbar(getActivity(), s);
        sendButton.setEnabled(true);
    }

    private void initVar() {
        typeFactory = new ChatRoomTypeFactoryImpl(this);
        templateChatFactory = new TemplateChatTypeFactoryImpl(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);

        adapter = new ChatRoomAdapter(typeFactory);
        adapter.setNav(getArguments().getString(PARAM_NAV, ""));
        mode = getArguments().getInt(PARAM_MODE, InboxChatViewModel.GET_CHAT_MODE);

        templateAdapter = new TemplateChatAdapter(templateChatFactory);

        if(needCreateWebSocket()) {
            presenter.getReply(mode);
        }else {
            progressBar.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
        templateAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);
        templateRecyclerView.setAdapter(templateAdapter);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        templateLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        templateRecyclerView.setLayoutManager(templateLayoutManager);

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

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
                return false;
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
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerInboxChatComponent daggerInboxChatComponent =
                (DaggerInboxChatComponent) DaggerInboxChatComponent.builder()
                        .appComponent(appComponent).build();
        daggerInboxChatComponent.inject(this);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void finishLoading() {
    }

    @Override
    public void setHeader() {

        if (toolbar != null) {
            mainHeader.setVisibility(View.VISIBLE);
            avatar = toolbar.findViewById(R.id.user_avatar);
            user = toolbar.findViewById(R.id.title);
            onlineDesc = toolbar.findViewById(R.id.subtitle);
            label = toolbar.findViewById(R.id.label);
            ImageHandler.loadImageCircle2(getActivity(), avatar, getArguments().getString(PARAM_SENDER_IMAGE), R.drawable.ic_image_avatar_boy);
            user.setText(getArguments().getString(PARAM_SENDER_NAME));
            if (!TextUtils.isEmpty(getArguments().getString(PARAM_SENDER_TAG, ""))
                    && !getArguments().getString(PARAM_SENDER_TAG, "").equals(ListChatViewHolder
                    .USER)) {
                label.setText(getArguments().getString(PARAM_SENDER_TAG));
                label.setVisibility(View.VISIBLE);
            } else {
                label.setVisibility(View.GONE);
            }
            setOnlineDesc(getActivity().getString(R.string.just_now));
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
                    TextView title = notifier.findViewById(R.id.title);
                    View action = notifier.findViewById(R.id.action);
                    title.setText(R.string.error_no_connection_retrying);
                    action.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    @Override
    public void scrollToBottom() {
        recyclerView.scrollToPosition(adapter.getItemCount());
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
                    presenter.addDummyMessage(response);
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

    @Override
    public void onErrorSendReply() {
        adapter.removeLast();
        setViewEnabled(true);
        finishLoading();
        replyColumn.setText("");
        showError(getActivity().getString(R.string.delete_error).concat("\n").concat(getString(R.string.string_general_error)));
    }

    @Override
    public void resetReplyColumn() {
        replyColumn.setText("");
    }

    @Override
    public boolean isMyMessage(int fromUid) {
        return String.valueOf(fromUid).equals(SessionHandler.getLoginID(MainApplication.getAppContext()));
    }

    @Override
    public void setTemplate(List<Visitable> listTemplate) {
        if (listTemplate == null) {
            templateRecyclerView.setVisibility(View.GONE);
        } else {
            templateRecyclerView.setVisibility(View.VISIBLE);
            templateAdapter.setList(listTemplate);
        }
    }


    @Override
    public boolean isCurrentThread(int msgId) {
        return getArguments().getString(PARAM_MESSAGE_ID).equals(String.valueOf(msgId));
    }

    @Override
    public void setViewEnabled(boolean isEnabled) {

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


    @Override
    public void addDummyInitialMessage() {
        MyChatViewModel item = new MyChatViewModel();
        item.setMsg(getReplyMessage());
        item.setReplyTime(MyChatViewModel.SENDING_TEXT);
        item.setDummy(true);
        item.setSenderId(getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID));
        adapter.addReply(item);
        scrollToBottom();
    }

    @Override
    public void disableAction() {
        sendButton.setEnabled(false);
    }


    private void setResult() {
        if (adapter != null && getActivity() != null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            if (adapter.getLastItem() != null)
                bundle.putParcelable(PARCEL, adapter.getLastItem());
            intent.putExtras(bundle);
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
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
            avatar = toolbar.findViewById(R.id.user_avatar);
            user = toolbar.findViewById(R.id.title);
            onlineDesc = toolbar.findViewById(R.id.subtitle);
            label = toolbar.findViewById(R.id.label);
            ImageHandler.loadImageCircle2(getActivity(), avatar, null, R.drawable.ic_image_avatar_boy);
            user.setText(getArguments().getString(PARAM_SENDER_NAME));
            label.setText(getArguments().getString(PARAM_SENDER_TAG));
            setOnlineDesc(getActivity().getString(R.string.just_now));
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
                    setOnlineDesc(getActivity().getString(R.string.just_now));
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
            notifyConnectionWebSocket();
            presenter.recreateWebSocket();
        }
    }

    @Override
    public void onOpenWebSocket() {
        if (getActivity() != null && presenter != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView title = notifier.findViewById(R.id.title);
                    title.setText(R.string.connected_websocket);
                    View action = notifier.findViewById(R.id.action);
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
        } catch (Exception e) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> strings = data.getStringArrayListExtra("string");
                    templateAdapter.update(strings);
                    break;
                }
            default:
                break;
        }
    }
}
