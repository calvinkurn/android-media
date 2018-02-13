package com.tokopedia.inbox.inboxchat.uploadimage.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 9/5/17.
 */

public class UploadImagePojo {

    @SerializedName("message_status")
    @Expose
    private String messageStatus;
    @SerializedName("pic_obj")
    @Expose
    private String picObj;
    @SerializedName("pic_src")
    @Expose
    private String picSrc;
    @SerializedName("server_id")
    @Expose
    private String serverId;
    @SerializedName("success")
    @Expose
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
