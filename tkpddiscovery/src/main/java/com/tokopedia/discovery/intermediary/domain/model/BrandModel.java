package com.tokopedia.discovery.intermediary.domain.model;

/**
 * Created by alifa on 5/26/17.
 */

public class BrandModel {

    private String imageUrl="";
    private String id="";
    private String brandName="";

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
