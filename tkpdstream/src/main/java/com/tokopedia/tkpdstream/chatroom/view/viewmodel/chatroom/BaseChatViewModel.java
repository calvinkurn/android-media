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
    }

    BaseChatViewModel(String message, long createdAt, long updatedAt, String messageId,
                      String senderId, String senderName, String senderIconUrl,
                      boolean isInfluencer, boolean isAdministrator) {
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
        dest.writeByte((byte) (showHeaderTime ? 1 : 0));
        dest.writeString(message);
        dest.writeLong(createdAt);
        dest.writeLong(updatedAt);
        dest.writeString(formattedCreatedAt);
        dest.writeString(formattedUpdatedAt);
        dest.writeString(messageId);
        dest.writeLong(headerTime);
        dest.writeString(formattedHeaderTime);
        dest.writeString(senderId);
        dest.writeString(senderName);
        dest.writeString(senderIconUrl);
        dest.writeByte((byte) (isInfluencer ? 1 : 0));
        dest.writeByte((byte) (isAdministrator ? 1 : 0));
    }

    protected BaseChatViewModel(Parcel in) {
        showHeaderTime = in.readByte() != 0;
        message = in.readString();
        createdAt = in.readLong();
        updatedAt = in.readLong();
        formattedCreatedAt = in.readString();
        formattedUpdatedAt = in.readString();
        messageId = in.readString();
        headerTime = in.readLong();
        formattedHeaderTime = in.readString();
        senderId = in.readString();
        senderName = in.readString();
        senderIconUrl = in.readString();
        isInfluencer = in.readByte() != 0;
        isAdministrator = in.readByte() != 0;
    }

    public static final Creator<BaseChatViewModel> CREATOR = new Creator<BaseChatViewModel>() {
        @Override
        public BaseChatViewModel createFromParcel(Parcel in) {
            return new BaseChatViewModel(in);
        }

        @Override
        public BaseChatViewModel[] newArray(int size) {
            return new BaseChatViewModel[size];
        }
    };
}
