package com.tokopedia.inbox.rescenter.createreso.domain.model.createreso;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 05/09/17.
 */

public class UploadDomain {

    private final boolean isVideo;

    @Nullable
    private String picObj;
    @Nullable
    private String picSrc;

    @Nullable
    private boolean isSuccess;

    @Nullable
    private String successMessage;

    public UploadDomain(String picObj, String picSrc, boolean isVideo) {
        this.picObj = picObj;
        this.picSrc = picSrc;
        this.isVideo = isVideo;
    }

    @Nullable
    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(@Nullable String picObj) {
        this.picObj = picObj;
    }

    @Nullable
    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(@Nullable String picSrc) {
        this.picSrc = picSrc;
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

    public boolean isVideo() {
        return isVideo;
    }
}
