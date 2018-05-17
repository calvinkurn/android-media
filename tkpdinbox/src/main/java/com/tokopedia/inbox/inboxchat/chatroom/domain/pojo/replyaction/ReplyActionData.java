
package com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.replyaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplyActionData {

    @SerializedName("chat")
    @Expose
    private Chat chat;
    @SerializedName("is_success")
    @Expose
    private boolean isSuccess;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
