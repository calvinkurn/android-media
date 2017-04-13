package com.tokopedia.seller.product.domain.model;



/**
 * @author sebastianuskh on 4/10/17.
 */

public class ImageProductInputDomainModel {
    private String url;
    private String description;
    private String imagePath;
    private int picId;
    private String picObj;

    public String getUrl() {
        return url;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPicId() {
        return picId;
    }

    public String getPicObj() {
        return picObj;
    }
}
