package com.tokopedia.inbox.inboxchat.viewmodel;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageViewModel {

   private boolean isSuccess;

    public SendMessageViewModel(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
