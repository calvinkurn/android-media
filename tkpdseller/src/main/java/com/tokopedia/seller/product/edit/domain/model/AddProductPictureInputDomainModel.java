package com.tokopedia.seller.product.edit.domain.model;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductPictureInputDomainModel {
    private int serverId;
    private ProductPhotoListDomainModel productPhotos;
    private String hostUrl;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public ProductPhotoListDomainModel getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos(ProductPhotoListDomainModel productPhotos) {
        this.productPhotos = productPhotos;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }
}
