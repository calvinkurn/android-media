
package com.tokopedia.inbox.inboxchat.domain.model.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReplyData {

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

    public List<ListReply> getList() {
        return list;
    }

    public void setList(List<ListReply> list) {
        this.list = list;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getTextAreaReply() {
        return textAreaReply;
    }

    public void setTextAreaReply(int textareaReply) {
        this.textAreaReply = textareaReply;
    }

    public float getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(float serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getTimeMachineStatus() {
        return timeMachineStatus;
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
