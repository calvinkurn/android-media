package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 7/5/17.
 */

public class Attachments {
    @SerializedName("fullUrl")
    private String fullUrl;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("isVideo")
    private int isVideo;

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
    }
}
