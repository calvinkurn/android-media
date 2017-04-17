package com.tokopedia.seller.product.domain.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductPictureInputDomainModel {
    private String duplicate;
    private String serverId;
    private ProductPhotoListDomainModel productPhotos;

    public String getDuplicate() {
        return duplicate;
    }

    public String getServerId() {
        return serverId;
    }

    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public ProductPhotoListDomainModel getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos(ProductPhotoListDomainModel productPhotos) {
        this.productPhotos = productPhotos;
    }
}
