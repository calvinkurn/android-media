package com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.imageupload;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.chatroom.data.mapper.WebSocketMapper;
import com.tokopedia.inbox.inboxchat.chatroom.domain.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.chatroom.view.presenter.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.SendableViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.DummyChatViewModel;

/**
 * Created by stevenfredian on 11/28/17.
 */

public class ImageUploadViewModel extends SendableViewModel implements
        Visitable<ChatRoomTypeFactory> {

    private String imageUrl;
    private String imageUrlThumbnail;
    private boolean isRetry;

    /**
     * Constructor for API.
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
     *                          !! startTime is not returned from API
     */
    public ImageUploadViewModel(String messageId, String fromUid, String from,
                                String fromRole, String attachmentId,
                                String attachmentType, String replyTime,
                                boolean isSender, String imageUrl,
                                String imageUrlThumbnail, boolean isRead, String message) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime,
                "", isRead, false, isSender, message);
        this.isRetry = false;
        this.imageUrl = imageUrl;
        this.imageUrlThumbnail = imageUrlThumbnail;
    }

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
     * @param startTime         start uploading time in START_TIME_FORMAT {@link SendableViewModel}
     */
    public ImageUploadViewModel(String messageId, String fromUid, String from,
                                String fromRole, String attachmentId,
                                String attachmentType, String replyTime,
                                boolean isSender, String imageUrl,
                                String imageUrlThumbnail, String startTime, String message) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime,
                startTime, false, false, isSender, message);
        this.isRetry = false;
        this.imageUrl = imageUrl;
        this.imageUrlThumbnail = imageUrlThumbnail;
    }

    /**
     * Constructor for sending image.
     *
     * @param fromUid      user id
     * @param attachmentId temporary attachment id
     * @param fileLoc      file location
     * @param startTime    start uploading time in START_TIME_FORMAT {@link SendableViewModel}
     */
    public ImageUploadViewModel(String fromUid, String attachmentId, String fileLoc, String
            startTime) {
        super("", fromUid, "", "", attachmentId,
                WebSocketMapper.TYPE_IMAGE_UPLOAD, DummyChatViewModel.SENDING_TEXT, startTime,
                false, true, true, "");
        this.isRetry = false;
        this.imageUrl = fileLoc;
        this.imageUrlThumbnail = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUrlThumbnail() {
        return imageUrlThumbnail;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
