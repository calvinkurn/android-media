package com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost;

/**
 * Created by nisie on 4/3/17.
 */

public class GenerateHostData {

    private String uploadHost;
    private String serverId;
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
