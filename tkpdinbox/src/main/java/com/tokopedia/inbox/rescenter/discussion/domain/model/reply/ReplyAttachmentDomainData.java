package com.tokopedia.inbox.rescenter.discussion.domain.model.reply;

/**
 * Created by hangnadi on 7/5/17.
 */

public class ReplyAttachmentDomainData {
    private int isVideo;
    private String fullUrl;
    private String thumbnail;

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
