package com.tokopedia.inbox.rescenter.discussion.data.pojo.createpicture;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 4/3/17.
 */

public class CreatePictureEntity {

    @SerializedName("file_uploaded")
    @Expose
    private String fileUploaded;
    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;

    public String getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }
}
