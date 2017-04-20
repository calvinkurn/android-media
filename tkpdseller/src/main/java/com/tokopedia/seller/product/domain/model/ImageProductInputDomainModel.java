package com.tokopedia.seller.product.domain.model;



/**
 * @author sebastianuskh on 4/10/17.
 */

public class ImageProductInputDomainModel {
    private String url;
    private String description;
    private String imagePath;
    private String picObj;
    private int picId;
    private int productId;

    public ImageProductInputDomainModel() {
        url = "";
        description = "";
        imagePath = "";
        picObj = "";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    public int getProductId() {
        return productId;
    }
}
