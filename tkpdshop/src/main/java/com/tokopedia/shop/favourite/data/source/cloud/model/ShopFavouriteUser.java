package com.tokopedia.shop.favourite.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopFavouriteUser {
    @SerializedName("user_image")
    @Expose
    private String imageUrl;
    @SerializedName("user_name")
    @Expose
    private String name;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}