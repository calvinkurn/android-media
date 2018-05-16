package com.tokopedia.inbox.inboxchat.viewmodel.chatroom.imageannouncement;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.WebSocketMapper;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;

/**
 * @author by nisie on 5/15/18.
 */
public class ImageAnnouncementViewModel extends BaseChatViewModel implements Visitable<ChatRoomTypeFactory> {

    private String imageUrl;
    private String redirectUrl;

    /**
     * Constructor for WebSocketResponse / API Response
     * {@link ChatWebSocketListenerImpl}
     * {@link GetReplyListUseCase}
     *
     * @param messageId      messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     *                       {@link WebSocketMapper} types
     * @param replyTime      replytime in unixtime
     * @param imageUrl       image url
     * @param redirectUrl    redirect url in http
     */
    public ImageAnnouncementViewModel(String messageId, String fromUid, String from,
                                      String fromRole, String attachmentId, String attachmentType,
                                      String replyTime, String imageUrl, String redirectUrl,
                                      String message) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message);
        this.imageUrl = imageUrl;
        this.redirectUrl = redirectUrl;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

}
