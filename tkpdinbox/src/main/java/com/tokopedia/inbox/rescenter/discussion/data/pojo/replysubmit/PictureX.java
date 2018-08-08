package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 7/5/17.
 */

public class PictureX {
    @SerializedName("fullUrl")
    private String fullUrl;
    @SerializedName("thumbnail")
    private String thumbnail;

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
}
