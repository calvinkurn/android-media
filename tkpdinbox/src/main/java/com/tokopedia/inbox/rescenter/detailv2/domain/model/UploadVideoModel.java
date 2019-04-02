package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by hangnadi on 6/5/17.
 */

public class UploadVideoModel {

    private boolean success;
    private String messageError;
    private UploadImageData uploadVideoData;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public UploadImageData getUploadVideoData() {
        return uploadVideoData;
    }

    public void setUploadVideoData(UploadImageData uploadImageData) {
        this.uploadVideoData = uploadImageData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
