package com.tokopedia.inbox.rescenter.discussion.data.pojo.generatehost;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 7/12/17.
 */

public class GenerateHostV2Entity {


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
