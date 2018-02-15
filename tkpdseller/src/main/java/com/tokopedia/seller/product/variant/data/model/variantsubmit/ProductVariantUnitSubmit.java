package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.PictureItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 8/15/2017.
 */
@Deprecated
public class ProductVariantUnitSubmit implements Parcelable {

    @SerializedName("v")
    @Expose
    private long variantId;
    @SerializedName("vu")
    @Expose
    private long variantUnitId;
    @SerializedName("pos")
    @Expose
    private int position;
    @SerializedName("opt")
    @Expose
    private List<ProductVariantOptionSubmit> productVariantOptionSubmitList;


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
     * Option list for this variant. example "merah:1" "hijau:2"
     *
     * @return Option List
     */
    public List<ProductVariantOptionSubmit> getProductVariantOptionSubmitList() {
        return productVariantOptionSubmitList;
    }

    /**
     * set Option list
     *
     * @param productVariantOptionSubmitList example "merah:1" "hijau:2"
     */
    public void setProductVariantOptionSubmitList(List<ProductVariantOptionSubmit> productVariantOptionSubmitList) {
        this.productVariantOptionSubmitList = productVariantOptionSubmitList;
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
        dest.writeList(this.productVariantOptionSubmitList);
    }

    public ProductVariantUnitSubmit() {
    }

    protected ProductVariantUnitSubmit(Parcel in) {
        this.variantId = in.readLong();
        this.variantUnitId = in.readLong();
        this.position = in.readInt();
        this.productVariantOptionSubmitList = new ArrayList<ProductVariantOptionSubmit>();
        in.readList(this.productVariantOptionSubmitList, ProductVariantOptionSubmit.class.getClassLoader());
    }

    public static final Creator<ProductVariantUnitSubmit> CREATOR = new Creator<ProductVariantUnitSubmit>() {
        @Override
        public ProductVariantUnitSubmit createFromParcel(Parcel source) {
            return new ProductVariantUnitSubmit(source);
        }

        @Override
        public ProductVariantUnitSubmit[] newArray(int size) {
            return new ProductVariantUnitSubmit[size];
        }
    };
}