package com.tokopedia.inbox.inboxchat.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.reply.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.DummyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyViewModel;

import java.util.List;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class ChatRoomContract {

    public interface View extends CustomerView{

        Bundle getArguments();

        void setHeader();

        void displayReplyField(boolean b);

        ChatRoomAdapter getAdapter();

        Context getContext();

        String getReplyMessage();

        String  getString(int error_empty_report);

        void showError(String string);

        void setViewEnabled(boolean b);

        void addDummyMessage(String dummyText);

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

        @Deprecated
        boolean isCurrentThread(int msgId);

        boolean isCurrentThread(String msgId);

        boolean isMyMessage(int fromUid);

        boolean isMyMessage(String fromUid);

        void setTemplate(List<Visitable> listTemplate);

        void addTemplateString(String message);

        void goToSettingTemplate();

        void onGoToGallery(Attachment attachment, String fullTime);

        void onGoToImagePreview(String imageUrl, String replyTime);

        void onGoToWebView(String attachment, String id);

        boolean needCreateWebSocket();

        void hideNotifier();

        void onSuccessInitMessage();

        void disableAction();

        void onErrorInitMessage(String s);

        boolean isAllowedTemplate();

        Fragment getFragment();

        void onErrorUploadImages(String throwable, MyChatViewModel model);

        void onRetrySend(MyChatViewModel attachment);

        void onRetrySendImage(ImageUploadViewModel element);

        void onSuccessSendAttach(ReplyActionData data, MyChatViewModel model);

        void setUploadingMode(boolean b);

        void scrollToBottomWithCheck();

        void setHeaderModel(String nameHeader, String imageHeader);

        void startAttachProductActivity(String shopId, String shopName, boolean isSeller);

        void productClicked(Integer productId, String productName, String productPrice, Long dateTime, String url);

        boolean isChatBot();

        void onQuickReplyClicked(QuickReplyViewModel quickReply);

        void showQuickReplyView(QuickReplyListViewModel model);

        void onInvoiceSelected(SelectedInvoice selectedInvoice);

        void onClickRating(OppositeChatViewModel element, int rating);

        void onSuccessSetRating(OppositeChatViewModel element);

        void onErrorSetRating();

        void showSearchInvoiceScreen();

        boolean shouldHandleUrlManually(String url);

        void showSnackbarError(String string);

        UserSession getUserSession();

    }

    interface Presenter extends CustomerPresenter<View>{
        void getReply(int mode);

        void getAttachProductDialog(String shopId,String shopName, String senderRole);

        void onOpenWebSocket();

        void closeWebSocket();

        void onGoToDetail(String userId, String role);

        void sendMessage(int networkType);

        void sendMessage(int networkType, String reply);

        void addMessageChatBalloon(WebSocketResponse response);

        void initMessage(String s, String string, String string1, String string2);

        void openCamera();

        void startUpload(List<DummyChatViewModel> list, int network);

        String getFileLocFromCamera();

        void setChatRating(OppositeChatViewModel model, int userId, int rating);
    }
}
