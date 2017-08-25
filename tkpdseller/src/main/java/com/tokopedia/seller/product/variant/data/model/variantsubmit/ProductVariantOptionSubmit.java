package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 8/15/2017.
 */

public class ProductVariantOptionSubmit implements Parcelable {

    @SerializedName("pvo")
    @Expose
    private long productVariantOptionId;
    @SerializedName("vuv")
    @Expose
    private long variantUnitValueId;
    @SerializedName("t_id")
    @Expose
    private long temporaryId;
    @SerializedName("cstm")
    @Expose
    private String customText;

    /**
     * Product variant option, set with the id got from server if there is any
     * if it is new, set the id as "0"
     *
     * @return Product variant option id from server. 0 if new created
     */
    public long getProductVariantOptionId() {
        return productVariantOptionId;
    }

    /**
     * Product variant option, set with the id got from server if there is any
     * if it is new, set the id as "0"
     *
     * @param productVariantOptionId "0" if new, previous id from server if existing
     */
    public void setProductVariantOptionId(long productVariantOptionId) {
        this.productVariantOptionId = productVariantOptionId;
    }

    /**
     * if custom, 0. if not custom, id of the variant unit value
     *
     * @return variant unit value. 0 if custom. id if not custom. example: "1" for "putih"
     */
    public long getVariantUnitValueId() {
        return variantUnitValueId;
    }

    /**
     * if custom, set to 0
     * if not custom, set with the id of the variant unit value
     *
     * @param variantUnitValueId 0 if custom, id if not custom. ex: "1" for "putih"
     */
    public void setVariantUnitValueId(long variantUnitValueId) {
        this.variantUnitValueId = variantUnitValueId;
    }

    /**
     * Template id, start from 1, 2, 3, to determine the id when submit product variant
     *
     * @return example: "1", "2", "3"
     */
    public long getTemporaryId() {
        return temporaryId;
    }

    /**
     * Template id, start from 1, 2, 3, to determine the id when submit product variant
     *
     * @param temporaryId example: "1", "2", "3"
     */
    public void setTemporaryId(long temporaryId) {
        this.temporaryId = temporaryId;
    }

    /**
     * @return example: "Merah kehijauan", "Ukuran 32.5"
     */
    public String getCustomText() {
        return customText;
    }

    /**
     * @param customText example: "Merah kehijauan", "Ukuran 32.5"
     */
    public void setCustomText(String customText) {
        this.customText = customText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.productVariantOptionId);
        dest.writeLong(this.variantUnitValueId);
        dest.writeLong(this.temporaryId);
        dest.writeString(this.customText);
    }

    public ProductVariantOptionSubmit() {
    }

    protected ProductVariantOptionSubmit(Parcel in) {
        this.productVariantOptionId = in.readLong();
        this.variantUnitValueId = in.readLong();
        this.temporaryId = in.readLong();
        this.customText = in.readString();
    }

    public static final Creator<ProductVariantOptionSubmit> CREATOR = new Creator<ProductVariantOptionSubmit>() {
        @Override
        public ProductVariantOptionSubmit createFromParcel(Parcel source) {
            return new ProductVariantOptionSubmit(source);
        }

        @Override
        public ProductVariantOptionSubmit[] newArray(int size) {
            return new ProductVariantOptionSubmit[size];
        }
    };
}
