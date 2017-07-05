package com.tokopedia.inbox.rescenter.discussion.data.pojo.generatehost;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 4/3/17.
 */

public class GenerateHostEntity {


    @SerializedName("server_id")
    private String serverId;
    @SerializedName("upload_host")
    private String uploadHost;
    @SerializedName("token")
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
