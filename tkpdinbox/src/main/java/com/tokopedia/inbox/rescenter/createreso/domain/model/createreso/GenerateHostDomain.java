package com.tokopedia.inbox.rescenter.createreso.domain.model.createreso;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 05/09/17.
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
