package com.tokopedia.inbox.rescenter.discussion.domain.model;

/**
 * Created by nisie on 3/31/17.
 */

public class LoadMoreAttachment {

    private String url;
    private String imageThumb;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImageThumb() {
        return imageThumb;
    }
}
