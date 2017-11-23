
package com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductImage {

    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("image_src_300")
    @Expose
    private String imageSrc300;
    @SerializedName("image_status")
    @Expose
    private int imageStatus;
    @SerializedName("image_description")
    @Expose
    private String imageDescription;
    @SerializedName("image_primary")
    @Expose
    private int imagePrimary;
    @SerializedName("image_src")
    @Expose
    private String imageSrc;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageSrc300() {
        return imageSrc300;
    }

    public void setImageSrc300(String imageSrc300) {
        this.imageSrc300 = imageSrc300;
    }

    public int getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(int imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public int getImagePrimary() {
        return imagePrimary;
    }

    public void setImagePrimary(int imagePrimary) {
        this.imagePrimary = imagePrimary;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

}
