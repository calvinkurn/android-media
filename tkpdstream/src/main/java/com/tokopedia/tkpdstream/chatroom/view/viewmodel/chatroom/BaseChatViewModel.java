package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.tkpdstream.common.util.TimeConverter;

/**
 * @author by nisie on 2/15/18.
 */

public class BaseChatViewModel implements Parcelable {

    private boolean showHeaderTime;
    private String message;
    private long createdAt;
    private long updatedAt;
    private String formattedCreatedAt;
    private String formattedUpdatedAt;
    private String messageId;
    private long headerTime;
    private String formattedHeaderTime;

    private String senderId;
    private String senderName;
    private String senderIconUrl;
    private boolean isInfluencer;
    private boolean isAdministrator;
    private boolean canVibrate;

    BaseChatViewModel(String message, long createdAt, long updatedAt, String messageId) {
        this.showHeaderTime = false;
        this.headerTime = 0;
        this.formattedHeaderTime = "tes";
        this.message = message.replace("\\n", "\n");
        this.createdAt = createdAt;
        this.updatedAt = updatedAt != 0 ? updatedAt : createdAt;
        this.formattedCreatedAt = TimeConverter.convertToHourFormat(this.createdAt);
        this.formattedUpdatedAt = TimeConverter.convertToHourFormat(this.updatedAt);
        this.messageId = messageId;
        this.senderId = "";
        this.senderName = "";
        this.senderIconUrl = "";
        this.isInfluencer = false;
        this.isAdministrator = false;
        this.canVibrate = false;
    }

    BaseChatViewModel(String message, long createdAt, long updatedAt, String messageId,
                      String senderId, String senderName, String senderIconUrl,
                      boolean isInfluencer, boolean isAdministrator, boolean canVibrate) {
        this.showHeaderTime = false;
        this.headerTime = 0;
        this.formattedHeaderTime = "tes";
        this.message = message.replace("\\n", "\n");
        this.createdAt = createdAt;
        this.updatedAt = updatedAt != 0 ? updatedAt : createdAt;
        this.formattedCreatedAt = TimeConverter.convertToHourFormat(this.createdAt);
        this.formattedUpdatedAt = TimeConverter.convertToHourFormat(this.updatedAt);
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderIconUrl = senderIconUrl;
        this.isInfluencer = isInfluencer;
        this.isAdministrator = isAdministrator;
        this.canVibrate = canVibrate;
    }

    public boolean isShowHeaderTime() {
        return showHeaderTime;
    }

    public void setShowHeaderTime(boolean showHeaderTime) {
        this.showHeaderTime = showHeaderTime;
    }

    public String getMessage() {
        return message;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }

    public String getFormattedUpdatedAt() {
        return formattedUpdatedAt;
    }

    public void setHeaderTime(long headerTime) {
        this.headerTime = headerTime;
        this.formattedHeaderTime = TimeConverter.convertToHeaderDisplay(headerTime);
    }

    public long getHeaderTime() {
        return headerTime;
    }

    public String getFormattedHeaderTime() {
        return formattedHeaderTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderIconUrl() {
        return senderIconUrl;
    }

    public boolean isInfluencer() {
        return isInfluencer;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.showHeaderTime ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeLong(this.createdAt);
        dest.writeLong(this.updatedAt);
        dest.writeString(this.formattedCreatedAt);
        dest.writeString(this.formattedUpdatedAt);
        dest.writeString(this.messageId);
        dest.writeLong(this.headerTime);
        dest.writeString(this.formattedHeaderTime);
        dest.writeString(this.senderId);
        dest.writeString(this.senderName);
        dest.writeString(this.senderIconUrl);
        dest.writeByte(this.isInfluencer ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAdministrator ? (byte) 1 : (byte) 0);
    }

    protected BaseChatViewModel(Parcel in) {
        this.showHeaderTime = in.readByte() != 0;
        this.message = in.readString();
        this.createdAt = in.readLong();
        this.updatedAt = in.readLong();
        this.formattedCreatedAt = in.readString();
        this.formattedUpdatedAt = in.readString();
        this.messageId = in.readString();
        this.headerTime = in.readLong();
        this.formattedHeaderTime = in.readString();
        this.senderId = in.readString();
        this.senderName = in.readString();
        this.senderIconUrl = in.readString();
        this.isInfluencer = in.readByte() != 0;
        this.isAdministrator = in.readByte() != 0;
    }

    public static final Creator<BaseChatViewModel> CREATOR = new Creator<BaseChatViewModel>() {
        @Override
        public BaseChatViewModel createFromParcel(Parcel source) {
            return new BaseChatViewModel(source);
        }

        @Override
        public BaseChatViewModel[] newArray(int size) {
            return new BaseChatViewModel[size];
        }
    };
}
