
package com.tokopedia.inbox.inboxchat.domain.model;

import android.text.Spanned;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.MessageViewModel;

import java.util.Calendar;

public abstract class ListReplyViewModel implements
        Visitable<ChatRoomTypeFactory> {

    private int msgId;
    private int userId;
    private int replyId;
    private String senderId;
    private String senderName;
    private String role;
    private String msg;
    private Spanned spanned;
    private String replyTime;
    private long replyTimeNano;
    private int fraudStatus;
    private String readTime;
    private Attachment attachment;
    private int attachmentId;
    private int oldMsgId;
    private boolean showTime;
    private boolean showHour;
    private boolean isOpposite;
    private boolean isHighlight;
    private String oldMessageTitle;
    private boolean showRating;
    private int ratingStatus;

    public ListReplyViewModel() {
    }

//    public ListReplyViewModel(int msgId,
//                              int userId,
//                              int replyId,
//                              String senderId,
//                              String senderName,
//                              String role,
//                              String msg,
//                              Spanned spanned,
//                              String replyTime,
//                              int fraudStatus,
//                              String readTime,
//                              Attachment attachment,
//                              int attachmentId,
//                              int oldMsgId,
//                              boolean showTime,
//                              boolean showHour,
//                              boolean isOpposite,
//                              boolean isHighlight,
//                              String oldMessageTitle,
//                              boolean showRating,
//                              int ratingStatus,
//                              String toUserId,
//                              FallbackAttachmentViewModel fallbackAttachmentViewModel) {
//        super(String.valueOf(msgId), senderId, senderName, role, toUserId, new MessageViewModel()
//                , String.valueOf(attachmentId), attachment.getType(), fallbackAttachmentViewModel);
//        this.userId = userId;
//        this.replyId = replyId;
//        this.msg = msg;
//        this.spanned = spanned;
//        this.replyTime = replyTime;
//        this.fraudStatus = fraudStatus;
//        this.readTime = readTime;
//        this.attachment = attachment;
//        this.oldMsgId = oldMsgId;
//        this.showTime = showTime;
//        this.showHour = showHour;
//        this.isOpposite = isOpposite;
//        this.isHighlight = isHighlight;
//        this.oldMessageTitle = oldMessageTitle;
//        this.showRating = showRating;
//        this.ratingStatus = ratingStatus;
//    }

    public ListReplyViewModel(int msgId,
                              int userId,
                              int replyId,
                              String senderId,
                              String senderName,
                              String role,
                              String msg,
                              Spanned spanned,
                              String replyTime,
                              int fraudStatus,
                              String readTime,
                              Attachment attachment,
                              int attachmentId,
                              int oldMsgId,
                              boolean showTime,
                              boolean showHour,
                              boolean isOpposite,
                              boolean isHighlight,
                              String oldMessageTitle,
                              boolean showRating,
                              int ratingStatus) {
        this.msgId = msgId;
        this.userId = userId;
        this.replyId = replyId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.role = role;
        this.msg = msg;
        this.spanned = spanned;
        this.replyTime = replyTime;
        this.fraudStatus = fraudStatus;
        this.readTime = readTime;
        this.attachment = attachment;
        this.attachmentId = attachmentId;
        this.oldMsgId = oldMsgId;
        this.showTime = showTime;
        this.showHour = showHour;
        this.isOpposite = isOpposite;
        this.isHighlight = isHighlight;
        this.oldMessageTitle = oldMessageTitle;
        this.showRating = showRating;
        this.ratingStatus = ratingStatus;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public Calendar getReplyCalendar() {
        return ChatTimeConverter.unixToCalendar(Long.parseLong(replyTime));
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public int getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(int fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public int getOldMsgId() {
        return oldMsgId;
    }

    public void setOldMsgId(int oldMsgId) {
        this.oldMsgId = oldMsgId;
    }

    public void setShowTime(boolean b) {
        showTime = b;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public boolean isShowHour() {
        return showHour;
    }

    public void setShowHour(boolean showHour) {
        this.showHour = showHour;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isOpposite() {
        return isOpposite;
    }

    public void setOpposite(boolean opposite) {
        isOpposite = opposite;
    }

    public boolean isHighlight() {
        return isHighlight;
    }

    public void setHighlight(boolean highlight) {
        isHighlight = highlight;
    }

    public Spanned getSpanned() {
        return spanned;
    }

    public void setSpanned(Spanned spanned) {
        this.spanned = spanned;
    }

    public String getOldMessageTitle() {
        return oldMessageTitle;
    }

    public void setOldMessageTitle(String oldMessageTitle) {
        this.oldMessageTitle = oldMessageTitle;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public boolean isShowRating() {
        return showRating;
    }

    public void setShowRating(boolean showRating) {
        this.showRating = showRating;
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

    public void setReplyTimeNano(long replyTimeNano) {
        this.replyTimeNano = replyTimeNano;
    }
}
