package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 8/15/2017.
 * do not remove. still used in version 1.14 for draft
 */
@Deprecated
public class ProductVariantDataSubmit implements Parcelable {

    @SerializedName("variant")
    @Expose
    private List<ProductVariantUnitSubmit> productVariantUnitSubmitList = new ArrayList<>();
    @SerializedName("product_variant")
    @Expose
    private List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList = new ArrayList<>();

    /**
     * Summary of the selected variant list. for example. Color and size.
     * @return variant List of the selected colors (ex:"white", red") and sizes ("43", "44")
     */
    public List<ProductVariantUnitSubmit> getProductVariantUnitSubmitList() {
        return productVariantUnitSubmitList;
    }

    /**
     * Summary of the selected variant list. for example. Color and size.
     * @param productVariantUnitSubmitList List of the selected colors (ex:"white", red") and sizes ("43", "44")
     */
    public void setProductVariantUnitSubmitList(List<ProductVariantUnitSubmit> productVariantUnitSubmitList) {
        this.productVariantUnitSubmitList = productVariantUnitSubmitList;
    }

    /**
     * Detail of the combination for variant level 1+ variant level 2 + so on
     * @return  productVariantCombinationSubmitList list of the metrics of selected variants
     */
    public List<ProductVariantCombinationSubmit> getProductVariantCombinationSubmitList() {
        return productVariantCombinationSubmitList;
    }

    /**
     * Detail of the combination for variant level 1+ variant level 2 + so on
     * @param productVariantStatus list of the metrics of selected variants
     */
    public void setProductVariantCombinationSubmitList(List<ProductVariantCombinationSubmit> productVariantStatus) {
        this.productVariantCombinationSubmitList = productVariantStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.productVariantUnitSubmitList);
        dest.writeTypedList(this.productVariantCombinationSubmitList);
    }

    public ProductVariantDataSubmit() {
    }

    protected ProductVariantDataSubmit(Parcel in) {
        this.productVariantUnitSubmitList = in.createTypedArrayList(ProductVariantUnitSubmit.CREATOR);
        this.productVariantCombinationSubmitList = in.createTypedArrayList(ProductVariantCombinationSubmit.CREATOR);
    }

    public static final Parcelable.Creator<ProductVariantDataSubmit> CREATOR = new Parcelable.Creator<ProductVariantDataSubmit>() {
        @Override
        public ProductVariantDataSubmit createFromParcel(Parcel source) {
            return new ProductVariantDataSubmit(source);
        }

        @Override
        public ProductVariantDataSubmit[] newArray(int size) {
            return new ProductVariantDataSubmit[size];
        }
    };
}
