package com.tokopedia.tkpd.tkpdfeed.feedplus.view.util;

/**
 * @author by nisie on 5/18/17.
 */

public class ShareModel {
    private String url;
    private String title;
    private String imageUrl;
    private String contentMessage;
    private String pageRowNumber;

    public ShareModel(String url, String title, String imageUrl, String contentMessage, String
            pageRowNumber) {
        this.url = url;
        this.title = title;
        this.imageUrl = imageUrl;
        this.contentMessage = contentMessage;
        this.pageRowNumber = pageRowNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public String getPageRowNumber() {
        return pageRowNumber;
    }
}
