package com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by nisie on 5/15/18.
 */
public class ListReplyResponse {
    @SerializedName("contacts")
    @Expose
    private java.util.List<Contact> contacts = null;
    @SerializedName("list")
    @Expose
    private List<ListReply> list = null;

    @SerializedName("paging_next")
    @Expose
    private boolean hasNext;

    @SerializedName("textarea_reply")
    @Expose
    private int textAreaReply;

    @SerializedName("server_process_time")
    @Expose
    private float serverProcessTime;
    @SerializedName("server")
    @Expose
    private String server;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("time_machine_status")
    @Expose
    private int timeMachineStatus;

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<ListReply> getList() {
        return list;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public int getTextAreaReply() {
        return textAreaReply;
    }

    public float getServerProcessTime() {
        return serverProcessTime;
    }

    public String getServer() {
        return server;
    }

    public String getStatus() {
        return status;
    }

    public int getSuccess() {
        return success;
    }

    public int getTimeMachineStatus() {
        return timeMachineStatus;
    }
}
