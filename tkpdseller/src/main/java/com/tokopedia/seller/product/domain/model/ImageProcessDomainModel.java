package com.tokopedia.seller.product.domain.model;

/**
 * @author sebastianuskh on 4/18/17.
 */

public class ImageProcessDomainModel {
    private int picId;
    private String url;
    private String picObj;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    public int getPicId() {
        return picId;
    }

    public String getUrl() {
        return url;
    }

    public String getPicObj() {
        return picObj;
    }
}
