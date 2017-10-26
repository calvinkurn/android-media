package com.tokopedia.inbox.inboxchat.viewmodel;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class ChatConversationViewModel {

    String replyId;
    String senderId;
    String name;
    boolean isOpposite;
    String label;
    String time;
    String date;
    boolean isHighlight;

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String id) {
        this.replyId = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpposite() {
        return isOpposite;
    }

    public void setOpposite(boolean opposite) {
        isOpposite = opposite;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isHighlight() {
        return isHighlight;
    }

    public void setHighlight(boolean highlight) {
        isHighlight = highlight;
    }
}
