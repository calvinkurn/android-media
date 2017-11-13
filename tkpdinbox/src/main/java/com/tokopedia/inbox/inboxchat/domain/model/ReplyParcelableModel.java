
package com.tokopedia.inbox.inboxchat.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReplyParcelableModel implements Parcelable{

    public ReplyParcelableModel(String messageId, String msg, String replyTime) {
        this.messageId = messageId;
        this.msg = msg;
        this.replyTime = replyTime;
    }

    private String messageId;
    private String msg;
    private String replyTime;


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.messageId);
        dest.writeString(this.msg);
        dest.writeString(this.replyTime);
    }

    protected ReplyParcelableModel(Parcel in) {
        this.messageId = in.readString();
        this.msg = in.readString();
        this.replyTime = in.readString();
    }

    public static final Creator<ReplyParcelableModel> CREATOR = new Creator<ReplyParcelableModel>() {
        @Override
        public ReplyParcelableModel createFromParcel(Parcel source) {
            return new ReplyParcelableModel(source);
        }

        @Override
        public ReplyParcelableModel[] newArray(int size) {
            return new ReplyParcelableModel[size];
        }
    };
}
