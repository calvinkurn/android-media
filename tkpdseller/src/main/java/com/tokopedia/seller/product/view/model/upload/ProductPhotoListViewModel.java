package com.tokopedia.seller.product.view.model.upload;

import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

class ProductPhotoListViewModel {
    private List<ImageProductInputServiceModel> photos;
    private int productDefaultPicture;

    public List<ImageProductInputServiceModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageProductInputServiceModel> photos) {
        this.photos = photos;
    }

    public int getProductDefaultPicture() {
        return productDefaultPicture;
    }

    public void setProductDefaultPicture(int productDefaultPicture) {
        this.productDefaultPicture = productDefaultPicture;
    }
}
