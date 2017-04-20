package com.tokopedia.seller.product.view.model.upload;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ImageProductInputViewModel {
    private String imagePath;
    private String imageDescription;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }
}
