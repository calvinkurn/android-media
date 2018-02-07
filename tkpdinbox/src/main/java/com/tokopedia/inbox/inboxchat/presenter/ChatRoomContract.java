package com.tokopedia.inbox.inboxchat.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;

import java.util.List;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class ChatRoomContract {

    public interface View extends CustomerView{

        Bundle getArguments();

        void showLoading();

        void finishLoading();

        void setHeader();

        void setTextAreaReply(boolean b);

        ChatRoomAdapter getAdapter();

        Context getContext();

        String getReplyMessage();

        String  getString(int error_empty_report);

        void showError(String string);

        void setViewEnabled(boolean b);

        void addDummyMessage();

        void setCanLoadMore(boolean hasNext);

        void scrollToBottom();

        void hideMainLoading();

        void setOnlineDesc(String s);

        WebSocketInterface getInterface();

        void onGoToTimeMachine(String url);

        void addTimeMachine();

        void addUrlToReply(String url);

        String getKeyword();

        void setResult(ChatRoomViewModel model);

        void notifyConnectionWebSocket();

        void startActivity(Intent instance);

        Context getActivity();

        void onSuccessSendReply(ReplyActionData data, String reply);

        void onErrorSendReply();

        void resetReplyColumn();

        boolean isCurrentThread(int msgId);

        boolean isMyMessage(int fromUid);

        void setTemplate(List<Visitable> listTemplate);

        void addTemplateString(String message);

        void goToSettingTemplate();

        void onGoToGallery(Attachment attachment);

        void onGoToWebView(String attachment, String id);

        boolean needCreateWebSocket();

        void hideNotifier();

        void onSuccessInitMessage();

        void addDummyInitialMessage();

        void disableAction();

        void onErrorInitMessage(String s);

        boolean isAllowedTemplate();
    }

    interface Presenter extends CustomerPresenter<View>{
        void getReply(int mode);

        void getAttachProductDialog(String shopId, String senderRole);

        void onOpenWebSocket();

        void closeWebSocket();

        void onGoToDetail(String userId, String role);

        void sendMessageWithApi();

        void addDummyMessage(WebSocketResponse response);

        void initMessage(String s, String string, String string1, String string2);
    }
}
