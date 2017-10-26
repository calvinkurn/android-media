package com.tokopedia.inbox.inboxchat.presenter;

import com.google.gson.JsonObject;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.domain.model.GetReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxchat.presenter.subscriber.GetReplySubscriber;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import rx.Subscriber;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_MESSAGE_ID;

/**
 * Created by stevenfredian on 9/26/17.
 */

public class ChatRoomPresenter extends BaseDaggerPresenter<ChatRoomContract.View> implements ChatRoomContract.Presenter{

    private final GetMessageListUseCase getMessageListUseCase;
    private final GetReplyListUseCase getReplyListUseCase;
    private final ReplyMessageUseCase replyMessageUseCase;
    public PagingHandler pagingHandler;
    boolean isRequesting;
    private OkHttpClient client;
    private WebSocket ws;
    private String magicString;
    private ChatWebSocketListenerImpl listener;
    private boolean flagTyping;

    @Inject
    ChatRoomPresenter(GetMessageListUseCase getMessageListUseCase,
                       GetReplyListUseCase getReplyListUseCase,
                       ReplyMessageUseCase replyMessageUseCase){
        this.getMessageListUseCase = getMessageListUseCase;
        this.getReplyListUseCase = getReplyListUseCase;
        this.replyMessageUseCase = replyMessageUseCase;
    }

    @Override
    public void attachView(ChatRoomContract.View view) {
        super.attachView(view);
        isRequesting=false;
        this.pagingHandler = new PagingHandler();

        client = new OkHttpClient();
        magicString = "wss://chat-staging.tokopedia.com/connect?" +
                "os_type=1" +
                "&device_id="+ GCMHandler.getRegistrationId(getView().getContext()) +
                "&user_id="+ SessionHandler.getLoginID(getView().getContext());
        listener = new ChatWebSocketListenerImpl(getView().getInterface());
        recreateWebSocket();
    }

    @Override
    public void detachView() {
        super.detachView();
        client.dispatcher().executorService().shutdown();
    }

    public void recreateWebSocket() {
        Request request = new Request.Builder().url(magicString)
                .header("Origin", "https://staging.tokopedia.com")
                .build();
        ws = client.newWebSocket(request, listener);
    }

    public void onGoToProfile(String s) {

    }

    public void onLoadMore() {
        if (!isRequesting) {
            pagingHandler.nextPage();
            getReply();
        }else{
            getView().finishLoading();
        }
    }


    public void getReply() {
        isRequesting = true;
        getReplyListUseCase.execute(GetReplyListUseCase.generateParam(getView().getArguments().getString(PARAM_MESSAGE_ID), pagingHandler.getPage()), new GetReplySubscriber(getView(), this));
    }

    public void setResult(ChatRoomViewModel replyData) {
        getView().setCanLoadMore(false);
        getView().setHeader();
        if(pagingHandler.getPage()==1) {
            getView().getAdapter().setList(replyData.getChatList());
            getView().scrollToBottom();
            getView().hideMainLoading();
        }else {
            getView().getAdapter().addList(replyData.getChatList());
        }
        getView().setTextAreaReply(replyData.getTextAreaReply()==1);
        getView().setCanLoadMore(replyData.isHasNext());
    }

    public void finishRequest() {
        isRequesting = false;
    }

    private boolean isValidReply() {
        boolean isValid = true;
        if (getView().getReplyMessage().trim().length() == 0) {
            isValid = false;
            getView().showError(getView().getString(R.string.error_empty_report));
        }
        return isValid;
    }

    public void sendMessage() {
        if(isValidReply()){
            getView().addDummyMessage();
            getView().setViewEnabled(false);

            final String reply = (getView().getReplyMessage());
            String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));

            RequestParams params = ReplyMessageUseCase.generateParam(messageId, reply);
            isRequesting = true;
            replyMessageUseCase.execute(params, new Subscriber<ReplyActionData>() {
                @Override
                public void onCompleted() {
                    isRequesting = false;
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ReplyActionData data) {
                    getView().onSuccessSendReply(data, reply);
                }
            });
        }
    }

    public void onRefresh() {
        if (!isRequesting) {
            pagingHandler.resetPage();
            getReply();
        } else {
//            getView().getRefreshHandler().finishRefresh();
        }
    }

    public void setIsTyping(String messageId) throws JSONException {
        if(!flagTyping){
            JSONObject json = new JSONObject();
            json.put("code",203);
            JSONObject data = new JSONObject();
            data.put("msg_id",Integer.valueOf(messageId));
            json.put("data", data);
            ws.send(json.toString());
            flagTyping = true;
        }
    }

    public void stopTyping(String messageId) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("code",204);
        JSONObject data = new JSONObject();
        data.put("msg_id",Integer.valueOf(messageId));
        json.put("data", data);
        ws.send(json.toString());
        flagTyping = false;
    }
}
