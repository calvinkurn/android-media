package com.tokopedia.seller.product.edit.domain.model;


import com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class ImageProductInputDomainModel {
    private String url;
    private String description;
    private String imagePath;
    private String picObj;
    private String picId;
    @ImageStatusTypeDef
    private int status;

    public ImageProductInputDomainModel() {
        url = "";
        description = "";
        imagePath = "";
        picObj = "";
    }

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

    public void setPicId(String picId) {
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

    public String getPicId() {
        return picId;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setStatus(@ImageStatusTypeDef int status) {
        this.status = status;
    }

    @ImageStatusTypeDef
    public int getStatus() {
        return status;
    }
}
