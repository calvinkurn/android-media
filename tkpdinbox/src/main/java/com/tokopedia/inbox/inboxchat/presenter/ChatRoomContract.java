package com.tokopedia.inbox.inboxchat.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;

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

        void onGoToGallery(Attachment attachment, String fullTime);

        void onGoToWebView(String attachment, String id);

        boolean needCreateWebSocket();

        void hideNotifier();

        void onSuccessInitMessage();

        void addDummyInitialMessage();

        void disableAction();

        void onErrorInitMessage(String s);

        boolean isAllowedTemplate();

        Fragment getFragment();

        void onErrorUploadImages(String throwable, MyChatViewModel model);

        void onRetrySend(MyChatViewModel attachment);

        void onSuccessSendAttach(ReplyActionData data, MyChatViewModel model);

        void setUploadingMode(boolean b);

        void scrollToBottomWithCheck();

        void setHeaderModel(String nameHeader, String imageHeader);

        void startAttachProductActivity(String shopId, String shopName, boolean isSeller);

        void productClicked(Integer productId, String productName, String productPrice, Long dateTime);
    }

    interface Presenter extends CustomerPresenter<View>{
        void getReply(int mode);

        void getAttachProductDialog(String shopId,String shopName, String senderRole);

        void onOpenWebSocket();

        void closeWebSocket();

        void onGoToDetail(String userId, String role);

        void sendMessageWithApi();

        void addDummyMessage(WebSocketResponse response);

        void initMessage(String s, String string, String string1, String string2);

        void openCamera();

        void startUpload(List<MyChatViewModel> list, int network);

        String getFileLocFromCamera();
    }
}
