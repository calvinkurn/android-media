package com.tokopedia.inbox.rescenter.discussion.data.pojo.uploadimage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 7/5/17.
 */

public class UploadVideoEntity {

    @SerializedName("message_status")
    private String messageStatus;
    @SerializedName("pic_obj")
    private String picObj;
    @SerializedName("pic_src")
    private String picSrc;
    @SerializedName("server_id")
    private String serverId;
    @SerializedName("success")
    private String success;

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
