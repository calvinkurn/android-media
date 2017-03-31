package com.tokopedia.inbox.rescenter.discussion.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 3/31/17.
 */

public class DiscussionAttachmentEntity {

    @SerializedName("imageThumb")
    private String imageThumb;

    @SerializedName("url")
    private String url;

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
