package com.tokopedia.seller.product.edit.data.source.cloud.model;

/**
 * @author sebastianuskh on 4/12/17.
 */

public class ProductPhotoServiceModel {
    private String url;
    private String description;
    private String picureId;
    private boolean defaultPicture;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDefault() {
        return defaultPicture;
    }

    public String getPicureId() {
        return picureId;
    }

    public void setPicureId(String picureId) {
        this.picureId = picureId;
    }

    public boolean isDefaultPicture() {
        return defaultPicture;
    }

    public void setDefaultPicture(boolean defaultPicture) {
        this.defaultPicture = defaultPicture;
    }
}
