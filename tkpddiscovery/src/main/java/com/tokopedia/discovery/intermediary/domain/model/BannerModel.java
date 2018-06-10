package com.tokopedia.discovery.intermediary.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alifa on 5/23/17.
 */

public class BannerModel {

    private Integer position;
    private String imageUrl;
    private String url;
    private String applink;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getApplink() {
        return applink;
    }
}
