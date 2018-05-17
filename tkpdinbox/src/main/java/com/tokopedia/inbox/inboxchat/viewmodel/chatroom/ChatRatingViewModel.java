package com.tokopedia.inbox.inboxchat.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.BaseChatViewModel;

/**
 * @author by yfsx on 14/05/18.
 */

public class ChatRatingViewModel extends BaseChatViewModel implements
        Visitable<ChatRoomTypeFactory> {
    public static final int RATING_NONE = 0;
    public static final int RATING_GOOD = 1;
    public static final int RATING_BAD = -1;

    private int ratingStatus;
    private long replyTimeNano;
    private String message;

    public ChatRatingViewModel(String msgId,
                               String fromUid,
                               String from,
                               String fromRole,
                               String message,
                               String attachmentId,
                               String attachmentType,
                               String replyTime,
                               int ratingStatus,
                               long replyTimeNano) {
        super(msgId, fromUid, from, fromRole,
                attachmentId, attachmentType, replyTime, message);
        this.message = message;
        this.ratingStatus = ratingStatus;
        this.replyTimeNano = replyTimeNano;
    }

    public int getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(int ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public long getReplyTimeNano() {
        return replyTimeNano;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
