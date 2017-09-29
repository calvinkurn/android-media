
package com.tokopedia.inbox.inboxchat.domain.model.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageData {

    @SerializedName("list")
    @Expose
    private List<ListMessage> list = null;
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

    public List<ListMessage> getList() {
        return list;
    }

    public void setList(List<ListMessage> list) {
        this.list = list;
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

}
