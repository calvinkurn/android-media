
package com.tokopedia.inbox.inboxchat.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;

import java.util.Calendar;

public class ReplyParcelableModel implements Parcelable{

    public ReplyParcelableModel(String senderId, String msg, String replyTime) {
        this.senderId = senderId;
        this.msg = msg;
        this.replyTime = replyTime;
    }

    private String senderId;
    private String msg;
    private String replyTime;


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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.senderId);
        dest.writeString(this.msg);
        dest.writeString(this.replyTime);
    }

    protected ReplyParcelableModel(Parcel in) {
        this.senderId = in.readString();
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
