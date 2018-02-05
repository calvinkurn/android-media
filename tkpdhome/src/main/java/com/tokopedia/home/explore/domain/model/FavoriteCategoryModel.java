package com.tokopedia.home.explore.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by errysuprayogi on 2/5/18.
 */

public class FavoriteCategoryModel {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("applinks")
    private String applinks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }
}
