package com.tokopedia.seller.product.data.source.db.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductPhotoListDraftModel {
    private List<ImageProductInputDraftModel> photos;
    private int productDefaultPicture;

    public List<ImageProductInputDraftModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageProductInputDraftModel> photos) {
        this.photos = photos;
    }

    public int getProductDefaultPicture() {
        return productDefaultPicture;
    }

    public void setProductDefaultPicture(int productDefaultPicture) {
        this.productDefaultPicture = productDefaultPicture;
    }
}
