package com.tokopedia.inbox.inboxchat.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.WebSocketMapper;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by stevenfredian on 11/28/17.
 */

public class ImageUploadViewModel extends BaseChatViewModel implements
        Visitable<ChatRoomTypeFactory> {

    private static final String START_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private boolean isRead;
    private boolean isDummy;
    private String imageUrl;
    private String imageUrlThumbnail;
    private boolean isRetry;
    private boolean isSender;
    private String startTime;

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
     * @param startTime start uploading time in START_TIME_FORMAT
     */
    public ImageUploadViewModel(String messageId, String fromUid, String from,
                                String fromRole, String attachmentId,
                                String attachmentType, String replyTime,
                                boolean isSender, String imageUrl,
                                String imageUrlThumbnail, String startTime) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime);
        this.isRead = false;
        this.isSender = isSender;
        this.isRetry = false;
        this.isDummy = false;
        this.imageUrl = imageUrl;
        this.imageUrlThumbnail = imageUrlThumbnail;
        this.startTime = startTime;
    }

    /**
     * Constructor for sending image.
     *
     * @param fromUid      user id
     * @param attachmentId temporary attachment id
     * @param fileLoc      file location
     */
    public ImageUploadViewModel(String fromUid, String attachmentId, String fileLoc) {
        super("", fromUid, "", "", attachmentId,
                WebSocketMapper.TYPE_IMAGE_UPLOAD, DummyChatViewModel.SENDING_TEXT);
        SimpleDateFormat date = new SimpleDateFormat(
                START_TIME_FORMAT, Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.isRead = false;
        this.isSender = true;
        this.isRetry = false;
        this.isDummy = true;
        this.imageUrl = fileLoc;
        this.imageUrlThumbnail = imageUrl;
        this.startTime = date.format(Calendar.getInstance().getTime());
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
