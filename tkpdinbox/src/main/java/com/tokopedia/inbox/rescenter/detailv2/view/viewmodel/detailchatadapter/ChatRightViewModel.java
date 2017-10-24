package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactory;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationActionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationCreateTimeDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationFlagDomain;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ChatRightViewModel implements Visitable<DetailChatTypeFactory> {

    private ConversationActionDomain action;
    private String message;
    private ConversationCreateTimeDomain createTime;
    private List<ConversationAttachmentDomain> attachment;
    private ConversationFlagDomain flag;

    public ChatRightViewModel() {
    }

    public ChatRightViewModel(ConversationActionDomain action,
                              String message,
                              ConversationCreateTimeDomain createTime,
                              List<ConversationAttachmentDomain> attachment,
                              ConversationFlagDomain flag) {
        this.action = action;
        this.message = message;
        this.createTime = createTime;
        this.attachment = attachment;
        this.flag = flag;
    }

    public ConversationActionDomain getAction() {
        return action;
    }

    public void setAction(ConversationActionDomain action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConversationCreateTimeDomain getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ConversationCreateTimeDomain createTime) {
        this.createTime = createTime;
    }

    public List<ConversationAttachmentDomain> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<ConversationAttachmentDomain> attachment) {
        this.attachment = attachment;
    }

    public ConversationFlagDomain getFlag() {
        return flag;
    }

    public void setFlag(ConversationFlagDomain flag) {
        this.flag = flag;
    }

    @Override
    public int type(DetailChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
