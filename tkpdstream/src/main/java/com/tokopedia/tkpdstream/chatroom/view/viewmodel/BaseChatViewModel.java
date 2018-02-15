package com.tokopedia.tkpdstream.chatroom.view.viewmodel;

/**
 * @author by nisie on 2/15/18.
 */

public class BaseChatViewModel {

    String message;
    String createdAt;
    String updatedAt;
    String messageId;

    public BaseChatViewModel(String message, String createdAt, String updatedAt, String messageId) {
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messageId = messageId;
    }
}
