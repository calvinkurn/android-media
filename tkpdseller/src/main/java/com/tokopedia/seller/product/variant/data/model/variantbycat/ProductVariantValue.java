package com.tokopedia.seller.product.variant.data.model.variantbycat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.base.view.adapter.ItemIdType;
import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by hendry on 8/14/2017.
 */

public class ProductVariantValue implements Parcelable, ItemIdType {

    public static final int TYPE = 199349;

    @SerializedName("value_id")
    @Expose
    private long valueId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("hex_code")
    @Expose
    private String hexCode;
    @SerializedName("icon")
    @Expose
    private String icon;

    public String getId() {
        return String.valueOf(valueId);
    }

    public long getIdLong(){
        return valueId;
    }

    public long getValueId() {
        return valueId;
    }

    public String getValue() {
        return value;
    }

    public String getHexCode() {
        return hexCode;
    }

    public String getIcon() {
        return icon;
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

    // TODO hendry for test
    public ProductVariantValue(long id, String value) {
        this.valueId = id;
        this.value = value;
    }

    protected ProductVariantValue(Parcel in) {
        this.valueId = (Long) in.readValue(Integer.class.getClassLoader());
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

    @Override
    public int getType() {
        return TYPE;
    }
}
