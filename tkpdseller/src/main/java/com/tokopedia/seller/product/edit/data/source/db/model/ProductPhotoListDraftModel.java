package com.tokopedia.seller.product.edit.data.source.db.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductPhotoListDraftModel {
    @SerializedName("product_list_photos")
    private List<ImageProductInputDraftModel> photos;

    @SerializedName("product_default_picture")
    private int productDefaultPicture;

    @SerializedName("ori_product_default_picture")
    private int oriProductDefaultPicture;

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

    public int getOriProductDefaultPicture() {
        return oriProductDefaultPicture;
    }

    public void setOriProductDefaultPicture(int oriProductDefaultPicture) {
        this.oriProductDefaultPicture = oriProductDefaultPicture;
    }
}
