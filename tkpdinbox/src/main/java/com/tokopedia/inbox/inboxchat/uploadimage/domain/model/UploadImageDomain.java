package com.tokopedia.inbox.inboxchat.uploadimage.domain.model;

/**
 * @author by nisie on 9/5/17.
 */

public class UploadImageDomain {

    private String messageStatus;
    private String picObj;
    private String picSrc;
    private String serverId;
    private String success;

    public UploadImageDomain(String messageStatus, String picObj,
                             String picSrc, String serverId, String success) {
        this.messageStatus = messageStatus;
        this.picObj = picObj;
        this.picSrc = picSrc;
        this.serverId = serverId;
        this.success = success;
    }

    public UploadImageDomain(String picObj, String picSrc) {
        this.picObj = picObj;
        this.picSrc = picSrc;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public String getPicObj() {
        return picObj;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public String getServerId() {
        return serverId;
    }

    public String getSuccess() {
        return success;
    }
}
