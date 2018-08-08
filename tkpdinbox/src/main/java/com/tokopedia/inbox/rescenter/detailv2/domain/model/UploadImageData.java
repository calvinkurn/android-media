package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by nisie on 4/3/17.
 */

public class UploadImageData {
    private String imageUrl;
    private String picObj;
    private String picSrc;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public String getPicSrc() {
        return picSrc;
    }
}
