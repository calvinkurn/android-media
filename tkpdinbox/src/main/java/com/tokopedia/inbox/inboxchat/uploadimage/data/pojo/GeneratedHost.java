
package com.tokopedia.inbox.inboxchat.uploadimage.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneratedHost {

    @SerializedName("server_id")
    @Expose
    private String serverId;
    @SerializedName("upload_host")
    @Expose
    private String uploadHost;
    @SerializedName("user_id")
    @Expose
    private int userId;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
