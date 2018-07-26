package com.tokopedia.seller.product.edit.view.model.upload;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Deprecated
public class ProductPhotoListViewModel {
    private List<ImageProductInputViewModel> photos;
    private int productDefaultPicture;

    public ProductPhotoListViewModel() {
        photos = new ArrayList<>();
    }

    public List<ImageProductInputViewModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageProductInputViewModel> photos) {
        if (photos == null) {
            this.photos = new ArrayList<>();
        } else {
            this.photos = photos;
        }
    }

    public int getProductDefaultPicture() {
        return productDefaultPicture;
    }

    public void setProductDefaultPicture(int productDefaultPicture) {
        this.productDefaultPicture = productDefaultPicture;
    }
}
