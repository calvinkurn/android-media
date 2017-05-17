package com.tokopedia.seller.product.data.source.db.model;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ImageProductInputDraftModel {
    private String url;
    private String description;
    private String imagePath;
    private String picId;
    private String picObj;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }
}
