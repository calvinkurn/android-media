package com.tokopedia.seller.product.edit.domain.model;

/**
 * @author sebastianuskh on 4/18/17.
 */

public class ImageProcessDomainModel {
    private int picId;
    private String url;
    private String picObj;

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }
}
