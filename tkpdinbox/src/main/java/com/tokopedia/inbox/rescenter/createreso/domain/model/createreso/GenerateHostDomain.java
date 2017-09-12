package com.tokopedia.inbox.rescenter.createreso.domain.model.createreso;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 05/09/17.
 */

public class GenerateHostDomain {

    @Nullable
    private String serverId;
    @Nullable
    private String url;

    @Nullable
    private boolean isSuccess;

    @Nullable
    private String successMessage;

    public GenerateHostDomain(@Nullable String serverId, @Nullable String url) {
        this.serverId = serverId;
        this.url = url;
    }

    @Nullable
    public String getServerId() {
        return serverId;
    }

    public void setServerId(@Nullable String serverId) {
        this.serverId = serverId;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    public void setUrl(@Nullable String url) {
        this.url = url;
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
