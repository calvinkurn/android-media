package com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 05/09/17.
 */

public class GenerateHostResponse {

    @SerializedName("server_id")
    @Expose
    private String serverId;

    @SerializedName("upload_host")
    @Expose
    private String uploadHost;

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
