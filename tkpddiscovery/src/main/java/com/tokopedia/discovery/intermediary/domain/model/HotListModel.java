package com.tokopedia.discovery.intermediary.domain.model;

/**
 * Created by alifa on 3/30/17.
 */

public class HotListModel {

    private String id = "";
    private String imageUrl = "";
    private String title = "";
    private String url = "";

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
}
