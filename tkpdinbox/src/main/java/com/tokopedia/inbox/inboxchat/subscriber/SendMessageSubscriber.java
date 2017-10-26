package com.tokopedia.inbox.inboxchat.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.inboxchat.listener.SendChat;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;

import rx.Subscriber;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageSubscriber extends Subscriber<SendMessageViewModel> {

    private final SendChat.View view;

    public SendMessageSubscriber(SendChat.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {
        view.removeDummyMessage();
        view.setActionsEnabled(true);
    }

    @Override
    public void onError(Throwable e) {
        view.onErrorSendMessage(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SendMessageViewModel sendMessageViewModel) {
        view.onSuccessSendMessage();


    }
}
