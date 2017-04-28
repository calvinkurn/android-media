package com.tokopedia.inbox.rescenter.discussion.domain.model;

/**
 * Created by nisie on 4/3/17.
 */

public class CreatePictureModel {

    private boolean success;
    private String messageError;
    private CreatePictureData createPictureData;
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

    public CreatePictureData getUploadImageData() {
        return createPictureData;
    }

    public void setUploadImageData(CreatePictureData createPictureData) {
        this.createPictureData = createPictureData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
