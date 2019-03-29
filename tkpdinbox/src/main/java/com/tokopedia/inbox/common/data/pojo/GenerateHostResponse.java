package com.tokopedia.inbox.common.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 30/07/18.
 */

public class GenerateHostResponse {

    @SerializedName("server_id")
    @Expose
    private String serverId;

    @SerializedName("upload_host")
    @Expose
    private String uploadHost;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("token")
    @Expose
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "GenerateHostResponse{" +
                "serverId='" + serverId + '\'' +
                ", uploadHost='" + uploadHost + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

}
