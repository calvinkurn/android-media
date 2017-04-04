
package com.tokopedia.inbox.rescenter.discussion.data.pojo.generatehost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneratedHost {

    @SerializedName("upload_host")
    @Expose
    private String uploadHost;
    @SerializedName("server_id")
    @Expose
    private String serverId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
