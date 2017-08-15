package com.tokopedia.seller.product.variant.data.model.variantbycat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 8/14/2017.
 */

public class ProductVariantByCatModel implements Parcelable {
    @SerializedName("variant_id")
    @Expose
    private int variantId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("has_unit")
    @Expose
    private int hasUnit;
    @SerializedName("units")
    @Expose
    private List<ProductVariantUnit> units = null;

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHasUnit() {
        return hasUnit;
    }

    public void setHasUnit(int hasUnit) {
        this.hasUnit = hasUnit;
    }

    public List<ProductVariantUnit> getUnits() {
        return units;
    }

    public void setUnits(List<ProductVariantUnit> units) {
        this.units = units;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.variantId);
        dest.writeString(this.name);
        dest.writeString(this.identifier);
        dest.writeInt(this.status);
        dest.writeInt(this.hasUnit);
        dest.writeList(this.units);
    }

    public ProductVariantByCatModel() {
    }

    protected ProductVariantByCatModel(Parcel in) {
        this.variantId = in.readInt();
        this.name = in.readString();
        this.identifier = in.readString();
        this.status = in.readInt();
        this.hasUnit = in.readInt();
        this.units = new ArrayList<ProductVariantUnit>();
        in.readList(this.units, ProductVariantUnit.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProductVariantByCatModel> CREATOR = new Parcelable.Creator<ProductVariantByCatModel>() {
        @Override
        public ProductVariantByCatModel createFromParcel(Parcel source) {
            return new ProductVariantByCatModel(source);
        }

        @Override
        public ProductVariantByCatModel[] newArray(int size) {
            return new ProductVariantByCatModel[size];
        }
    };
}
