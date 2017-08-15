package com.tokopedia.seller.product.variant.data.model.variantbycat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 8/14/2017.
 */

public class ProductVariantValue implements Parcelable {
    @SerializedName("value_id")
    @Expose
    private Integer valueId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("hex_code")
    @Expose
    private String hexCode;
    @SerializedName("icon")
    @Expose
    private String icon;

    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.valueId);
        dest.writeString(this.value);
        dest.writeString(this.hexCode);
        dest.writeString(this.icon);
    }

    public ProductVariantValue() {
    }

    protected ProductVariantValue(Parcel in) {
        this.valueId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.value = in.readString();
        this.hexCode = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<ProductVariantValue> CREATOR = new Parcelable.Creator<ProductVariantValue>() {
        @Override
        public ProductVariantValue createFromParcel(Parcel source) {
            return new ProductVariantValue(source);
        }

        @Override
        public ProductVariantValue[] newArray(int size) {
            return new ProductVariantValue[size];
        }
    };
}
