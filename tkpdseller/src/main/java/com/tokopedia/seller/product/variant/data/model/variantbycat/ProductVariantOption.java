package com.tokopedia.seller.product.variant.data.model.variantbycat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.base.view.adapter.ItemIdType;
import com.tokopedia.seller.base.view.adapter.ItemPickerType;

/**
 * Created by hendry on 8/14/2017.
 */

public class ProductVariantOption implements Parcelable, ItemIdType, ItemPickerType {

    public static final int TYPE = 199349;

    @SerializedName("value_id")
    @Expose
    private int valueId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("hex_code")
    @Expose
    private String hexCode;
    @SerializedName("icon")
    @Expose
    private String icon;

    public ProductVariantOption(int id, String value, String hex, String icon){
        this.valueId = id;
        this.value = value;
        this.hexCode = hex;
        this.icon = icon;
    }

    public String getId() {
        return String.valueOf(valueId);
    }

    @Override
    public String getItemId() {
        return value;
    }

    public int getValueId() {
        return valueId;
    }

    public String getValue() {
        return value;
    }

    public String getHexCode() {
        return hexCode;
    }

    // this is for alias for picker type
    @Override
    public String getTitle() {
        return value;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.valueId);
        dest.writeString(this.value);
        dest.writeString(this.hexCode);
        dest.writeString(this.icon);
    }

    protected ProductVariantOption(Parcel in) {
        this.valueId = in.readInt();
        this.value = in.readString();
        this.hexCode = in.readString();
        this.icon = in.readString();
    }

    public static final Creator<ProductVariantOption> CREATOR = new Creator<ProductVariantOption>() {
        @Override
        public ProductVariantOption createFromParcel(Parcel source) {
            return new ProductVariantOption(source);
        }

        @Override
        public ProductVariantOption[] newArray(int size) {
            return new ProductVariantOption[size];
        }
    };
}
