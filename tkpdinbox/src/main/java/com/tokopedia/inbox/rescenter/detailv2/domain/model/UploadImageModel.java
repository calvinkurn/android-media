package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by nisie on 4/3/17.
 */

public class UploadImageModel {

    private boolean success;
    private String messageError;
    private UploadImageData uploadImageData;
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

    public UploadImageData getUploadImageData() {
        return uploadImageData;
    }

    public void setUploadImageData(UploadImageData uploadImageData) {
        this.uploadImageData = uploadImageData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
