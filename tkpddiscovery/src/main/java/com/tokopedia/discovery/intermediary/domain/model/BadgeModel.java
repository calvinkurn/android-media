package com.tokopedia.discovery.intermediary.domain.model;

/**
 * Created by alifa on 3/27/17.
 */

public class BadgeModel {

    private String imageUrl = "";
    private String title = "";

    public BadgeModel(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
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
}
