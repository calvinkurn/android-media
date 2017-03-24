package com.tokopedia.inbox.rescenter.history.domain.model;

/**
 * Created by hangnadi on 3/24/17.
 */

public class AttachmentAwbDomainData {
    private String thumbnailUrl;
    private String url;

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
