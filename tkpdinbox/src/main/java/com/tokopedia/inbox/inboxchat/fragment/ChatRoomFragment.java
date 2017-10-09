package com.tokopedia.inbox.inboxchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 9/19/17.
 */

public class ChatRoomFragment extends BaseDaggerFragment implements ChatRoomContract.View, InboxMessageConstant{

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
    RefreshHandler refreshHandler;

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
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.reply_list);
        replyView = rootView.findViewById(R.id.add_comment_area);
        sendButton = (ImageView) rootView.findViewById(R.id.send_but);
        replyColumn = (EditText) rootView.findViewById(R.id.new_comment);
        refreshHandler = new RefreshHandler(getActivity(), rootView, onRefresh());
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
    }

    private void initVar() {
        typeFactory = new ChatRoomTypeFactoryImpl(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoading();
        presenter.getReply();
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
                if(adapter.checkLoadMore(index)){
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
                            scrollToBottom();
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void finishLoading() {
        refreshHandler.finishRefresh();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setHeader() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(com.tokopedia.core.R.id.app_bar);

        if (toolbar != null) {
            avatar = (ImageView) toolbar.findViewById(com.tokopedia.core.R.id.user_avatar);
            user = (TextView) toolbar.findViewById(com.tokopedia.core.R.id.title);
            onlineDesc = (TextView) toolbar.findViewById(com.tokopedia.core.R.id.subtitle);
            label = (TextView) toolbar.findViewById(com.tokopedia.core.R.id.label);
            onlineStatus = (ImageView) toolbar.findViewById(R.id.online_status);
            ImageHandler.loadImageCircle2(getActivity(), avatar, null, com.tokopedia.core.R.drawable.ic_image_avatar_boy);
            user.setText(getArguments().getString(PARAM_SENDER_NAME));
            label.setText(getArguments().getString(PARAM_SENDER_TAG));
            setOnlineDesc("baru saja");
        }
    }

    @Override
    public void setTextAreaReply(boolean textAreaReply) {
        if (textAreaReply) {
            replyView.setVisibility(View.VISIBLE);
        }else {
            replyView.setVisibility(View.GONE);
        }
    }

    @Override
    public ChatRoomAdapter getAdapter() {
        return adapter;
    }

    public void setOnlineDesc(final String when){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onlineDesc.setText(when);
            }
        });
    }

    @Override
    public void scrollTo(int i) {
        layoutManager.scrollToPosition(i);
    }

    @Override
    public void scrollToBottom() {
        layoutManager.scrollToPosition(adapter.getList().size());
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
        adapter.addReply(new MyChatViewModel(
                replyData.getChat().getMsgId(),
                replyData.getChat().getSenderId(),
                replyData.getChat().getMsg(),
                replyData.getChat().getReplyTime(),
                0, "", 0, 0));
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
        MyChatViewModel item = new MyChatViewModel(getReplyMessage());
        adapter.addReply(item);
        scrollToBottom();
    }

    @Override
    public RefreshHandler getRefreshHandler() {
        return refreshHandler;
    }

    @Override
    public void setCanLoadMore(boolean hasNext) {
        if(hasNext){
            adapter.showLoading();
        }else {
            adapter.removeLoading();
        }
    }

}
