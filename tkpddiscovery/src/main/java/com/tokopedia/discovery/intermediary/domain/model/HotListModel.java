package com.tokopedia.discovery.intermediary.domain.model;

/**
 * Created by alifa on 3/30/17.
 */

public class HotListModel {

    private String id = "";
    private String imageUrl = "";
    private String imageUrlSquare = "";
    private String imageUrlBanner = "";
    private String title = "";
    private String url = "";
    private boolean isTracked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrlSquare() {
        return imageUrlSquare;
    }

    public void setImageUrlSquare(String imageUrlSquare) {
        this.imageUrlSquare = imageUrlSquare;
    }

    public String getImageUrlBanner() {
        return imageUrlBanner;
    }

    public void setImageUrlBanner(String imageUrlBanner) {
        this.imageUrlBanner = imageUrlBanner;
    }

    public boolean isTracked() {
        return isTracked;
    }

    public void setTracked(boolean tracked) {
        isTracked = tracked;
    }
}
