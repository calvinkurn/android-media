package com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.variantoption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariantOptionChildOriPicture {
    @SerializedName("original")
    @Expose
    private String original;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
