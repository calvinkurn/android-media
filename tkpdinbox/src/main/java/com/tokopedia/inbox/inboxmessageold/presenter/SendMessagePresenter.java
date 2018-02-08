package com.tokopedia.inbox.inboxmessageold.presenter;

/**
 * Created by Nisie on 5/26/16.
 */
public interface SendMessagePresenter {
    boolean isValidMessage();

    void doSendMessage();

    void onDestroyView();

}
