package com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit;

/**
 * Created by nisie on 4/4/17.
 */

public class AttachmentData {
    private Integer isVideo;
    private String realFileUrl;
    private String fileUrl;
    private String attId;

    public Integer getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(Integer isVideo) {
        this.isVideo = isVideo;
    }

    public String getRealFileUrl() {
        return realFileUrl;
    }

    public void setRealFileUrl(String realFileUrl) {
        this.realFileUrl = realFileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }
}
