package com.tokopedia.inbox.inboxchat.presenter;

import android.text.TextUtils;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.getproducturlutil.GetProductUrlUtil;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.activity.SendMessageActivity;
import com.tokopedia.inbox.inboxchat.domain.usecase.SendMessageUseCase;
import com.tokopedia.inbox.inboxchat.listener.SendChat;
import com.tokopedia.inbox.inboxchat.subscriber.SendMessageSubscriber;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;

import javax.inject.Inject;

/**
 * @author by nisie on 10/25/17.
 */

public class SendChatPresenter extends BaseDaggerPresenter<SendChat.View>
        implements SendChat.Presenter, InboxMessageConstant {

    private final SessionHandler sessionHandler;
    private final SendMessageUseCase sendMessageUseCase;

    @Inject
    public SendChatPresenter(SessionHandler sessionHandler,
                             SendMessageUseCase sendMessageUseCase) {
        this.sessionHandler = sessionHandler;
        this.sendMessageUseCase = sendMessageUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        sendMessageUseCase.unsubscribe();
    }

    @Override
    public void sendMessage(String message, String source, String toShopId, String toUserId) {
        getView().removeError();

        if (isValidMessage(message)) {
            getView().showDummyMessage(message);
            getView().setActionsEnabled(false);
            sendMessageUseCase.execute(SendMessageUseCase.getParam(
                    message,
                    toShopId,
                    toUserId,
                    source
            ), new SendMessageSubscriber(getView()));

        }
    }

    private boolean isValidMessage(String message) {
        Boolean isValid = true;

        if (message.trim().length() == 0) {
            isValid = false;
            getView().setContentError(getView().getString(R.string.error_field_required));
        }

        return isValid;
    }

    @Override
    public void getAttachProductDialog(String senderId, String senderRole) {
        String id = "0";

        if (senderRole.equals(SendMessageActivity.ROLE_SELLER)
                && !TextUtils.isEmpty(senderId))
            id = String.valueOf(senderId);
        else if (!TextUtils.isEmpty(sessionHandler.getShopID())
                && !sessionHandler.getShopID().equals("0")) {
            id = sessionHandler.getShopID();
        }

        GetProductUrlUtil getProd = GetProductUrlUtil.createInstance(getView().getContext(), id);
        getProd.getOwnShopProductUrl(new GetProductUrlUtil.OnGetUrlInterface() {
            @Override
            public void onGetUrl(String url) {
                getView().addUrlToReply(url);
            }
        });
    }
}
