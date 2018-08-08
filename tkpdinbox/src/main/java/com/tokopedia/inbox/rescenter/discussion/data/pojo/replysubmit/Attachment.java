
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment {

    @SerializedName("is_video")
    @Expose
    private Integer isVideo;
    @SerializedName("real_file_url")
    @Expose
    private String realFileUrl;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("att_id")
    @Expose
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
