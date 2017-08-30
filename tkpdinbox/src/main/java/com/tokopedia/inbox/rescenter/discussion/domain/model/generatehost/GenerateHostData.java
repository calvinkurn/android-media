package com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost;

/**
 * Created by nisie on 4/3/17.
 */

public class GenerateHostData {

    private String uploadHost;
    private String serverId;
    private String token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}
