package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import com.tokopedia.tkpdstream.common.util.TimeConverter;

/**
 * @author by nisie on 2/15/18.
 */

public class BaseChatViewModel {

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
}
