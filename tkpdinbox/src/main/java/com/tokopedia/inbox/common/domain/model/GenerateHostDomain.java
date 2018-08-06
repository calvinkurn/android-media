package com.tokopedia.inbox.common.domain.model;

import javax.annotation.Nullable;

/**
 * @author by yfsx on 30/07/18.
 */

public class GenerateHostDomain {

    @Nullable
    private String serverId;
    @Nullable
    private String uploadHost;
    @Nullable
    private String token;

    @Nullable
    private boolean isSuccess;

    @Nullable
    private String successMessage;

    public GenerateHostDomain(String serverId, String uploadHost, String token) {
        this.serverId = serverId;
        this.uploadHost = uploadHost;
        this.token = token;
    }

    @Nullable
    public String getServerId() {
        return serverId;
    }

    public void setServerId(@Nullable String serverId) {
        this.serverId = serverId;
    }

    @Nullable
    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(@Nullable String uploadHost) {
        this.uploadHost = uploadHost;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    public void setToken(@Nullable String token) {
        this.token = token;
    }

    @Nullable
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(@Nullable boolean success) {
        isSuccess = success;
    }

    @Nullable
    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(@Nullable String successMessage) {
        this.successMessage = successMessage;
    }
}
