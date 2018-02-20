package com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.variantoption;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariantOptionChildOriPicture implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.original);
        dest.writeString(this.thumbnail);
    }

    public ProductVariantOptionChildOriPicture() {
    }

    protected ProductVariantOptionChildOriPicture(Parcel in) {
        this.original = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Creator<ProductVariantOptionChildOriPicture> CREATOR = new Creator<ProductVariantOptionChildOriPicture>() {
        @Override
        public ProductVariantOptionChildOriPicture createFromParcel(Parcel source) {
            return new ProductVariantOptionChildOriPicture(source);
        }

        @Override
        public ProductVariantOptionChildOriPicture[] newArray(int size) {
            return new ProductVariantOptionChildOriPicture[size];
        }
    };
}
