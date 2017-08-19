package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 8/15/2017.
 */

public class VariantUnitSubmit implements Parcelable {

    @SerializedName("v")
    @Expose
    private long variantId;
    @SerializedName("vu")
    @Expose
    private long variantUnitId;
    @SerializedName("pos")
    @Expose
    private int position;
    @SerializedName("pv")
    @Expose
    private long productVariant;
    @SerializedName("opt")
    @Expose
    private List<VariantSubmitOption> variantSubmitOptionList;

    /**
     * Varian ID.
     *
     * @return variantId example: "1" for warna". "6" for "ukuran pakaian"
     */
    public long getVariantId() {
        return variantId;
    }

    /**
     * Varian ID.
     *
     * @param variantId example: "1" for warna". "6" for "ukuran pakaian"
     */
    public void setVariantId(long variantId) {
        this.variantId = variantId;
    }

    /**
     * get variant unit chosen for this variant
     *
     * @return for example: example: "8" for "Ukuran pakaian US"
     */
    public long getVariantUnitId() {
        return variantUnitId;
    }

    /**
     * Variant unit id got from server. Set to 0 if there is no variant unit
     *
     * @param variantUnitId Variant unit. example: "8" for "Ukuran pakaian US"
     */
    public void setVariantUnitId(long variantUnitId) {
        this.variantUnitId = variantUnitId;
    }

    /**
     * Position level of the variant
     *
     * @return example: "1" or "2" at most
     */
    public int getPosition() {
        return position;
    }

    /**
     * Position level of the variant
     *
     * @param position position of variant. Set to 1 if the top level. 2 for the second level.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * product variant id, set with the id got from server
     *
     * @return productVariant product variant id. example: 659143 for "warna"
     */
    public long getProductVariant() {
        return productVariant;
    }

    /**
     * product variant id, set with the id got from server
     *
     * @param productVariant product variant id. example: 659143 for "warna"
     */
    public void setProductVariant(int productVariant) {
        this.productVariant = productVariant;
    }

    /**
     * Option list for this variant. example "merah:1" "hijau:2"
     *
     * @return Option List
     */
    public List<VariantSubmitOption> getVariantSubmitOptionList() {
        return variantSubmitOptionList;
    }

    /**
     * set Option list
     *
     * @param variantSubmitOptionList example "merah:1" "hijau:2"
     */
    public void setVariantSubmitOptionList(List<VariantSubmitOption> variantSubmitOptionList) {
        this.variantSubmitOptionList = variantSubmitOptionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.variantId);
        dest.writeLong(this.variantUnitId);
        dest.writeInt(this.position);
        dest.writeLong(this.productVariant);
        dest.writeList(this.variantSubmitOptionList);
    }

    public VariantUnitSubmit() {
    }

    protected VariantUnitSubmit(Parcel in) {
        this.variantId = in.readLong();
        this.variantUnitId = in.readLong();
        this.position = in.readInt();
        this.productVariant = in.readLong();
        this.variantSubmitOptionList = new ArrayList<VariantSubmitOption>();
        in.readList(this.variantSubmitOptionList, VariantSubmitOption.class.getClassLoader());
    }

    public static final Creator<VariantUnitSubmit> CREATOR = new Creator<VariantUnitSubmit>() {
        @Override
        public VariantUnitSubmit createFromParcel(Parcel source) {
            return new VariantUnitSubmit(source);
        }

        @Override
        public VariantUnitSubmit[] newArray(int size) {
            return new VariantUnitSubmit[size];
        }
    };
}