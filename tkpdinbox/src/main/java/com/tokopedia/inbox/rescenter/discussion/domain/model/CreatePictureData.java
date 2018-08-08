package com.tokopedia.inbox.rescenter.discussion.domain.model;

/**
 * Created by nisie on 4/3/17.
 */

public class CreatePictureData {
    private String fileUploaded;
    private int isSuccess;

    public String getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}
