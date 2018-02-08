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
    private List<ProductVariantUnit> unitList = null;

    public int getVariantId() {
        return variantId;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getStatus() {
        return status;
    }

    public int getHasUnit() {
        return hasUnit;
    }

    public List<ProductVariantUnit> getUnitList() {
        return unitList;
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
        dest.writeTypedList(this.unitList);
    }

    public ProductVariantByCatModel() {
    }

    protected ProductVariantByCatModel(Parcel in) {
        this.variantId = in.readInt();
        this.name = in.readString();
        this.identifier = in.readString();
        this.status = in.readInt();
        this.hasUnit = in.readInt();
        this.unitList = in.createTypedArrayList(ProductVariantUnit.CREATOR);
    }

    public static final Creator<ProductVariantByCatModel> CREATOR = new Creator<ProductVariantByCatModel>() {
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
