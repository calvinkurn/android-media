package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 8/15/2017.
 */

public class ProductVariantSubmit implements Parcelable {
    @SerializedName("variant_data")
    @Expose
    private VariantData variantData;

    /**
     * consist of the user selection of
     * 1. Summary of variant and
     * 2. Detail of the combination for variant level 1+ variant level 2 + so on
     * @return variant data
     */
    public VariantData getVariantData() {
        return variantData;
    }

    /**
     * consist of the user selection of
     * 1. Summary of variant and
     * 2. Detail of the combination for variant level 1+ variant level 2 + so on
     * @param variantData
     */
    public void setVariantData(VariantData variantData) {
        this.variantData = variantData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.variantData, flags);
    }

    public ProductVariantSubmit() {
    }

    protected ProductVariantSubmit(Parcel in) {
        this.variantData = in.readParcelable(VariantData.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProductVariantSubmit> CREATOR = new Parcelable.Creator<ProductVariantSubmit>() {
        @Override
        public ProductVariantSubmit createFromParcel(Parcel source) {
            return new ProductVariantSubmit(source);
        }

        @Override
        public ProductVariantSubmit[] newArray(int size) {
            return new ProductVariantSubmit[size];
        }
    };

    public boolean hasAnyData(){
        return variantData!= null && variantData.getVariantUnitSubmitList() != null &&
                variantData.getVariantUnitSubmitList().size() > 0;
    }
}
