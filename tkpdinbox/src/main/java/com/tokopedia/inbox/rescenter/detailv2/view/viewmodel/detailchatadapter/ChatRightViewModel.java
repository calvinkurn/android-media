package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationActionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationCreateTimeDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationFlagDomain;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ChatRightViewModel implements Parcelable {

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.action, flags);
        dest.writeString(this.message);
        dest.writeParcelable(this.createTime, flags);
        dest.writeTypedList(this.attachment);
        dest.writeParcelable(this.flag, flags);
    }

    protected ChatRightViewModel(Parcel in) {
        this.action = in.readParcelable(ConversationActionDomain.class.getClassLoader());
        this.message = in.readString();
        this.createTime = in.readParcelable(ConversationCreateTimeDomain.class.getClassLoader());
        this.attachment = in.createTypedArrayList(ConversationAttachmentDomain.CREATOR);
        this.flag = in.readParcelable(ConversationFlagDomain.class.getClassLoader());
    }

    public static final Creator<ChatRightViewModel> CREATOR = new Creator<ChatRightViewModel>() {
        @Override
        public ChatRightViewModel createFromParcel(Parcel source) {
            return new ChatRightViewModel(source);
        }

        @Override
        public ChatRightViewModel[] newArray(int size) {
            return new ChatRightViewModel[size];
        }
    };
}
