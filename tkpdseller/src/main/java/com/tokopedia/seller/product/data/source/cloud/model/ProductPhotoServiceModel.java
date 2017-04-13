package com.tokopedia.seller.product.data.source.cloud.model;

/**
 * @author sebastianuskh on 4/12/17.
 */

public class ProductPhotoServiceModel {
    private String url;
    private String description;
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
}
