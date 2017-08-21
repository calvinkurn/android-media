package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hendry on 8/15/2017.
 */

public class VariantData implements Parcelable {

    @SerializedName("variant")
    @Expose
    private List<VariantUnitSubmit> variantUnitSubmitList;
    @SerializedName("product_variant")
    @Expose
    private List<VariantStatus> variantStatusList;

    /**
     * Summary of the selected variant list. for example. Color and size.
     * @return variant List of the selected colors (ex:"white", red") and sizes ("43", "44")
     */
    public List<VariantUnitSubmit> getVariantUnitSubmitList() {
        return variantUnitSubmitList;
    }

    /**
     * Summary of the selected variant list. for example. Color and size.
     * @param variantUnitSubmitList List of the selected colors (ex:"white", red") and sizes ("43", "44")
     */
    public void setVariantUnitSubmitList(List<VariantUnitSubmit> variantUnitSubmitList) {
        this.variantUnitSubmitList = variantUnitSubmitList;
    }

    /**
     * Detail of the combination for variant level 1+ variant level 2 + so on
     * @return  variantStatusList list of the metrics of selected variants
     */
    public List<VariantStatus> getVariantStatusList() {
        return variantStatusList;
    }

    /**
     * Detail of the combination for variant level 1+ variant level 2 + so on
     * @param variantStatus list of the metrics of selected variants
     */
    public void setVariantStatusList(List<VariantStatus> variantStatus) {
        this.variantStatusList = variantStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.variantUnitSubmitList);
        dest.writeTypedList(this.variantStatusList);
    }

    public VariantData() {
    }

    protected VariantData(Parcel in) {
        this.variantUnitSubmitList = in.createTypedArrayList(VariantUnitSubmit.CREATOR);
        this.variantStatusList = in.createTypedArrayList(VariantStatus.CREATOR);
    }

    public static final Parcelable.Creator<VariantData> CREATOR = new Parcelable.Creator<VariantData>() {
        @Override
        public VariantData createFromParcel(Parcel source) {
            return new VariantData(source);
        }

        @Override
        public VariantData[] newArray(int size) {
            return new VariantData[size];
        }
    };
}
