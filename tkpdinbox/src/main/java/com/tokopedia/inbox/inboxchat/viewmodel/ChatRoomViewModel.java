package com.tokopedia.inbox.inboxchat.viewmodel;

import java.util.List;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class ChatRoomViewModel {

    String nameHeader;
    String labelHeader;
    String onlineTime;
    boolean onlineStatus;
    String imageHeader;
    List<ChatConversationViewModel> chatList;

    public String getNameHeader() {
        return nameHeader;
    }

    public void setNameHeader(String nameHeader) {
        this.nameHeader = nameHeader;
    }

    public String getLabelHeader() {
        return labelHeader;
    }

    public void setLabelHeader(String labelHeader) {
        this.labelHeader = labelHeader;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(String imageHeader) {
        this.imageHeader = imageHeader;
    }

    public List<ChatConversationViewModel> getChatList() {
        return chatList;
    }

    public void setChatList(List<ChatConversationViewModel> chatList) {
        this.chatList = chatList;
    }
}
