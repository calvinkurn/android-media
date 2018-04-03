package com.tokopedia.inbox.inboxchat.viewmodel;


import android.text.Spanned;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.model.ListReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.viewholder.MyChatViewHolder;

/**
 * Created by Hendri on 20/02/18.
 */

public class AttachProductViewModel extends ListReplyViewModel {

    boolean isSender;

    boolean isDummy;
    boolean readStatus;
    boolean isRetry;
    public AttachProductViewModel(boolean isSender) {
        super();
        this.isSender = isSender;
        this.isDummy = false;
        this.isRetry = false;
    }

    public AttachProductViewModel(OppositeChatViewModel source){
        super(source.getMsgId(),source.getUserId(),source.getReplyId(),source.getSenderId()
                ,source.getSenderName(),source.getRole(),source.getMsg(),source.getSpanned(),
                source.getReplyTime(),source.getFraudStatus(),source.getReadTime(),source.getAttachment(),
                source.getAttachmentId(),source.getOldMsgId(),source.isShowTime(),source.isShowHour(),
                source.isOpposite(),source.isHighlight(),source.getOldMessageTitle());
        this.isDummy = false;
        this.isRetry = false;
        this.isSender = false;
    }

    public AttachProductViewModel(ThumbnailChatViewModel source){
        super(source.getMsgId(),source.getUserId(),source.getReplyId(),source.getSenderId()
                ,source.getSenderName(),source.getRole(),source.getMsg(),source.getSpanned(),
                source.getReplyTime(),source.getFraudStatus(),source.getReadTime(),source.getAttachment(),
                source.getAttachmentId(),source.getOldMsgId(),source.isShowTime(),source.isShowHour(),
                source.isOpposite(),source.isHighlight(),source.getOldMessageTitle());
        this.isDummy = false;
        this.isRetry = false;
        this.isSender = false;
    }

    public AttachProductViewModel(MyChatViewModel source){
        super(source.getMsgId(),source.getUserId(),source.getReplyId(),source.getSenderId()
                ,source.getSenderName(),source.getRole(),source.getMsg(),source.getSpanned(),
                source.getReplyTime(),source.getFraudStatus(),source.getReadTime(),source.getAttachment(),
                source.getAttachmentId(),source.getOldMsgId(),source.isShowTime(),source.isShowHour(),
                source.isOpposite(),source.isHighlight(),source.getOldMessageTitle());
        this.isDummy = source.isDummy();
        this.readStatus = source.isReadStatus();
        this.isRetry = source.isRetry();
        this.isSender = true;
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
}
