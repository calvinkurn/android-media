package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.WebSocketMapper;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;

/**
 * Created by stevenfredian on 11/28/17.
 */

public class ImageUploadViewModel extends BaseChatViewModel implements
        Visitable<ChatRoomTypeFactory> {

    private boolean isRead;
    private boolean isDummy;
    private String imageUrl;
    private String imageUrlThumbnail;
    private boolean isRetry;
    private boolean isSender;

    /**
     * Constructor for WebSocket.
     * {@link ChatWebSocketListenerImpl}
     * {@link GetReplyListUseCase}
     *
     * @param messageId         messageId
     * @param fromUid           userId of sender
     * @param from              name of sender
     * @param fromRole          role of sender
     * @param attachmentId      attachment id
     * @param attachmentType    attachment type. Please refer to
     *                          {@link WebSocketMapper} types
     * @param replyTime         replytime in unixtime
     * @param imageUrl          image url
     * @param imageUrlThumbnail thumbnail image url
     */
    public ImageUploadViewModel(String messageId, String fromUid, String from,
                                String fromRole, String attachmentId,
                                String attachmentType, String replyTime,
                                boolean isSender, String imageUrl,
                                String imageUrlThumbnail) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime);
        this.isRead = false;
        this.isSender = isSender;
        this.isRetry = false;
        this.isDummy = false;
        this.imageUrl = imageUrl;
        this.imageUrlThumbnail = imageUrlThumbnail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUrlThumbnail() {
        return imageUrlThumbnail;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isDummy() {
        return isDummy;
    }

    @Override
    public int type(ChatRoomTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public boolean isRetry() {
        return isRetry;
    }

    public void setRetry(boolean isRetry) {
        this.isRetry = isRetry;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean isSender) {
        this.isSender = isSender;
    }
}
