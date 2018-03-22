
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductBrandViewModel implements Parcelable{

    @SerializedName("brand_id")
    @Expose
    private long brandId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("brand_status")
    @Expose
    private long brandStatus;

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public long getBrandStatus() {
        return brandStatus;
    }

    public void setBrandStatus(long brandStatus) {
        this.brandStatus = brandStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.brandId);
        dest.writeString(this.brandName);
        dest.writeLong(this.brandStatus);
    }

    public ProductBrandViewModel() {
    }

    protected ProductBrandViewModel(Parcel in) {
        this.brandId = in.readLong();
        this.brandName = in.readString();
        this.brandStatus = in.readLong();
    }

    public static final Creator<ProductBrandViewModel> CREATOR = new Creator<ProductBrandViewModel>() {
        @Override
        public ProductBrandViewModel createFromParcel(Parcel source) {
            return new ProductBrandViewModel(source);
        }

        @Override
        public ProductBrandViewModel[] newArray(int size) {
            return new ProductBrandViewModel[size];
        }
    };
}
