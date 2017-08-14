package com.tokopedia.seller.product.variant.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 8/14/2017.
 */

public class ProductVariantUnit implements Parcelable {
    @SerializedName("unit_id")
    @Expose
    private Integer unitId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("values")
    @Expose
    private List<ProductVariantValue> values = null;

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<ProductVariantValue> getValues() {
        return values;
    }

    public void setValues(List<ProductVariantValue> values) {
        this.values = values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.unitId);
        dest.writeString(this.name);
        dest.writeString(this.shortName);
        dest.writeList(this.values);
    }

    public ProductVariantUnit() {
    }

    protected ProductVariantUnit(Parcel in) {
        this.unitId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.shortName = in.readString();
        this.values = new ArrayList<ProductVariantValue>();
        in.readList(this.values, ProductVariantValue.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProductVariantUnit> CREATOR = new Parcelable.Creator<ProductVariantUnit>() {
        @Override
        public ProductVariantUnit createFromParcel(Parcel source) {
            return new ProductVariantUnit(source);
        }

        @Override
        public ProductVariantUnit[] newArray(int size) {
            return new ProductVariantUnit[size];
        }
    };
}
