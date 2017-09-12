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

    @SerializedName("url")
    @Expose
    private String url;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "GenerateHostResponse{" +
                "serverId='" + serverId + '\'' +
                "url='" + url + '\'' +
                '}';
    }

}
