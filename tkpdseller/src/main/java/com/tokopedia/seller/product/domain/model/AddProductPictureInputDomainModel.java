package com.tokopedia.seller.product.domain.model;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductPictureInputDomainModel {
    private String duplicate;
    private String productPhoto;
    private String productPhotoDefault;
    private String productPhotoDesc;
    private String serverId;

    public String getDuplicate() {
        return duplicate;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public String getProductPhotoDefault() {
        return productPhotoDefault;
    }

    public String getProductPhotoDesc() {
        return productPhotoDesc;
    }

    public String getServerId() {
        return serverId;
    }

    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public void setProductPhotoDefault(String productPhotoDefault) {
        this.productPhotoDefault = productPhotoDefault;
    }

    public void setProductPhotoDesc(String productPhotoDesc) {
        this.productPhotoDesc = productPhotoDesc;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
