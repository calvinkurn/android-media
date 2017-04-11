package com.tokopedia.seller.product.domain.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class UploadProductInputDomainModel {
    private List<ImageProductInputDomainModel> images;

    public List<ImageProductInputDomainModel> getImages() {
        return images;
    }

    public void setImages(List<ImageProductInputDomainModel> images) {
        this.images = images;
    }
}
