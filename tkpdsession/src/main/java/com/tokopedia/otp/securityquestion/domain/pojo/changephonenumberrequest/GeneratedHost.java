package com.tokopedia.otp.securityquestion.domain.pojo.changephonenumberrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 3/10/17.
 */

public class GeneratedHost {
    @SerializedName("server_id")
    @Expose
    int serverId;
    @SerializedName("upload_host")
    @Expose
    String uploadHost;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

}
