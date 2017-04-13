package com.tokopedia.seller.product.domain.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/12/17.
 */

public class ProductPhotoListDomainModel {
    List<ImageProductInputDomainModel> photos;
    int productDefaultPicture;

    public List<ImageProductInputDomainModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageProductInputDomainModel> photos) {
        this.photos = photos;
    }

    public int getProductDefaultPicture() {
        return productDefaultPicture;
    }

    public void setProductDefaultPicture(int productDefaultPicture) {
        this.productDefaultPicture = productDefaultPicture;
    }
}
