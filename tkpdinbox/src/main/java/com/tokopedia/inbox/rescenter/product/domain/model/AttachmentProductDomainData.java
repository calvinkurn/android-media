package com.tokopedia.inbox.rescenter.product.domain.model;

/**
 * Created by hangnadi on 3/29/17.
 */

public class AttachmentProductDomainData {
    private String thumbUrl;
    private String url;
    private boolean video;

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }
}
