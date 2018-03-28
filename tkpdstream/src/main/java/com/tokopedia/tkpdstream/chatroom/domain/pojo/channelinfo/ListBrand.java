
package com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListBrand {

    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("brand_url")
    @Expose
    private String brandUrl;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
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

    public String getBrandUrl() {
        return brandUrl;
    }

    public void setBrandUrl(String brandUrl) {
        this.brandUrl = brandUrl;
    }

}
