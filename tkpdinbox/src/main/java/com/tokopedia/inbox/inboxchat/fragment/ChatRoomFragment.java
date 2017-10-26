package com.tokopedia.inbox.inboxchat.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.activity.TimeMachineActivity;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomPresenter;
import com.tokopedia.inbox.inboxchat.util.Events;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessage.activity.InboxMessageDetailActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 9/19/17.
 */

public class ChatRoomFragment extends BaseDaggerFragment
        implements ChatRoomContract.View, InboxMessageConstant, WebSocketInterface {

    @Inject
    ChatRoomPresenter presenter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View replyView;

    private ArrayList<Integer> list;
    private ImageView avatar;
    private TextView user;
    private TextView onlineDesc;
    private TextView label;
    private ImageView onlineStatus;

    ChatRoomAdapter adapter;
    private ChatRoomTypeFactory typeFactory;
    private LinearLayoutManager layoutManager;
    private ImageView sendButton;
    private EditText replyColumn;
    private ImageView attachButton;
    RefreshHandler refreshHandler;
    private View mainHeader;
    private Toolbar toolbar;
    private Observable<String> replyWatcher;
    private Observable<Boolean> replyIsTyping;

    public static ChatRoomFragment createInstance(Bundle extras) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initVar();
        toolbar = (Toolbar) getActivity().findViewById(R.id.app_bar);
        mainHeader = toolbar.findViewById(R.id.main_header);
        mainHeader.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.reply_list);
        replyView = rootView.findViewById(R.id.add_comment_area);
        sendButton = (ImageView) rootView.findViewById(R.id.send_but);
        replyColumn = (EditText) rootView.findViewById(R.id.new_comment);
        attachButton = (ImageView) rootView.findViewById(R.id.add_url);
        refreshHandler = new RefreshHandler(getActivity(), rootView, onRefresh());
        replyWatcher = Events.text(replyColumn);
        recyclerView.setHasFixedSize(true);
        presenter.attachView(this);
        initListener();
        return rootView;
    }


    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefresh();
            }
        };
    }

    private void initListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendMessage();
            }
        });

        replyIsTyping = replyWatcher.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.length() > 0;
            }
        });

        replyIsTyping.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.i("call: ", "isTyping");
                try {
                    presenter.setIsTyping(getArguments().getString(InboxMessageDetailActivity
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
                        Log.i("call: ", "stopTyping");
                        try {
                            presenter.stopTyping(getArguments().getString(InboxMessageDetailActivity
                                    .PARAM_MESSAGE_ID));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getAttachProductDialog(
                        getArguments().getString(InboxMessageDetailActivity
                                .PARAM_SENDER_ID, ""),
                        getArguments().getString(InboxMessageDetailActivity.PARAM_SENDER_ROLE, "")
                );
            }
        });
    }

    @Override
    public void addUrlToReply(String url) {
        replyColumn.setText(replyColumn.getText() + "\n" + url);
        replyColumn.setSelection(replyColumn.length());
    }

    private void initVar() {
        typeFactory = new ChatRoomTypeFactoryImpl(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getReply();
        progressBar.setVisibility(View.VISIBLE);
        adapter = new ChatRoomAdapter(typeFactory);
        adapter.setNav(getArguments().getString(PARAM_NAV, ""));
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
        recyclerView.addOnLayoutChangeListener(onKeyboardShows());
    }


    private View.OnLayoutChangeListener onKeyboardShows() {
        return new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 100);
                }
            }
        };
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
        refreshHandler.finishRefresh();
    }

    @Override
    public void setHeader() {

        if (toolbar != null) {
            mainHeader.setVisibility(View.VISIBLE);
            avatar = (ImageView) toolbar.findViewById(R.id.user_avatar);
            user = (TextView) toolbar.findViewById(R.id.title);
            onlineDesc = (TextView) toolbar.findViewById(R.id.subtitle);
            label = (TextView) toolbar.findViewById(R.id.label);
            onlineStatus = (ImageView) toolbar.findViewById(R.id.online_status);
            ImageHandler.loadImageCircle2(getActivity(), avatar, null, R.drawable.ic_image_avatar_boy);
            user.setText(getArguments().getString(PARAM_SENDER_NAME));
            label.setText(getArguments().getString(PARAM_SENDER_TAG));
            setOnlineDesc("baru saja");
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onlineDesc.setText(when);
            }
        });
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
    public void scrollTo(int i) {
        layoutManager.scrollToPosition(i);
    }

    @Override
    public void scrollToBottom() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layoutManager.scrollToPosition(adapter.getList().size() - 1);
            }
        });
    }

    @Override
    public String getReplyMessage() {
        return replyColumn.getText().toString();
    }

    @Override
    public void showError(String error) {
        if (error.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), error);
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
    }

    @Override
    public void setViewEnabled(boolean isEnabled) {
//        replyColumn.setEnabled(isEnabled);
//        attachmentButton.setEnabled(isEnabled);
//        sendButton.setEnabled(isEnabled);
//        recyclerView.setEnabled(isEnabled);
//        if (isEnabled) {
//            header.setVisibility(View.VISIBLE);
//            replyView.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public void addDummyMessage() {
        MyChatViewModel item = new MyChatViewModel();
        item.setMsg(getReplyMessage());
        item.setReplyTime(MyChatViewModel.SENDING_TEXT);
        item.setDummy(true);
        adapter.addReply(item);
        scrollToBottom();
        setResult();
    }

    public void addDummyMessage(OppositeChatViewModel item) {
        adapter.addReply(item);
        scrollToBottom();
        setResult();
    }

    private void setResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("parcel", adapter.getLastItem());
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public RefreshHandler getRefreshHandler() {
        return refreshHandler;
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
        String senderId = data.getString("sender_id");
        if (senderId.equals(getArguments().get("sender_id"))) {
            OppositeChatViewModel item = new OppositeChatViewModel();
            item.setMsg(data.getString("summary"));
            item.setSenderId(senderId);
            item.setSenderName(data.getString("full_name"));
            item.setReplyTime(data.getString("create_time"));
            addDummyMessage(item);
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
                setOnlineDesc("sedang mengetik");
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING:
                setOnlineDesc("baru saja");
                break;
            default:
                break;
        }

    }

    @Override
    public void newWebSocket() {
        if (getActivity() != null)
            presenter.recreateWebSocket();
    }
}
