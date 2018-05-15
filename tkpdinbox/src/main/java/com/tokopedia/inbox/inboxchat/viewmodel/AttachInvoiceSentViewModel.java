package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;

/**
 * Created by Hendri on 27/03/18.
 */

public class AttachInvoiceSentViewModel extends BaseChatViewModel implements
        Visitable<ChatRoomTypeFactory> {

    private String imageUrl;
    private String description;
    boolean isSender;
    boolean isDummy;
    boolean readStatus;
    boolean isRetry;

    public AttachInvoiceSentViewModel(String msgId,
                                      String fromUid,
                                      String from,
                                      String fromRole,
                                      String attachmentId,
                                      String attachmentType,
                                      String replyTime,
                                      String description,
                                      String imageUrl,
                                      boolean isSender,
                                      boolean isDummy,
                                      boolean readStatus,
                                      boolean isRetry){
        super(msgId, fromUid, from, fromRole,
                attachmentId, attachmentType, replyTime);
        this.isDummy = isDummy;
        this.isRetry = isRetry;
        this.isSender = isSender;
        this.readStatus = readStatus;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public void setDummy(boolean dummy) {
        isDummy = dummy;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public boolean isRetry() {
        return isRetry;
    }

    public void setRetry(boolean retry) {
        isRetry = retry;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
