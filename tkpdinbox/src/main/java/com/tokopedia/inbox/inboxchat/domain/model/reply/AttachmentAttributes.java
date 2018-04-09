
package com.tokopedia.inbox.inboxchat.domain.model.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttachmentAttributes {

    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_url_thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_profile")
    @Expose
    private AttachmentProductProfile productProfile;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public AttachmentProductProfile getProductProfile() {
        return productProfile;
    }

    public void setProductProfile(AttachmentProductProfile productProfile) {
        this.productProfile = productProfile;
    }
}
