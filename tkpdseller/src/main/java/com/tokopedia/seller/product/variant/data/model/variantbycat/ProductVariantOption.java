package com.tokopedia.seller.product.variant.data.model.variantbycat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.base.view.adapter.ItemIdType;

/**
 * Created by hendry on 8/14/2017.
 */

public class ProductVariantOption implements Parcelable, ItemIdType {

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

    protected ProductVariantOption(Parcel in) {
        this.valueId = (Long) in.readValue(Integer.class.getClassLoader());
        this.value = in.readString();
        this.hexCode = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<ProductVariantOption> CREATOR = new Parcelable.Creator<ProductVariantOption>() {
        @Override
        public ProductVariantOption createFromParcel(Parcel source) {
            return new ProductVariantOption(source);
        }

        @Override
        public ProductVariantOption[] newArray(int size) {
            return new ProductVariantOption[size];
        }
    };

    @Override
    public int getType() {
        return TYPE;
    }
}
