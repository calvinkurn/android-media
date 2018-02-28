package com.tokopedia.seller.product.variant.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.adapter.ItemIdType;

/**
 * Created by nathan on 8/4/17.
 */
@Deprecated
public class ProductVariantDetailViewModel implements ItemIdType, Parcelable {

    public static final int TYPE = 199249;

    private long valueId;
    private String name;

    public String getId() {
        return String.valueOf(valueId);
    }

    public long getValueId() {
        return valueId;
    }

    public String getName() {
        return name;
    }

    public ProductVariantDetailViewModel(long id, String name) {
        this.valueId = id;
        this.name = name;
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
        dest.writeLong(this.valueId);
        dest.writeString(this.name);
    }

    protected ProductVariantDetailViewModel(Parcel in) {
        this.valueId = in.readLong();
        this.name = in.readString();
    }

    public static final Creator<ProductVariantDetailViewModel> CREATOR = new Creator<ProductVariantDetailViewModel>() {
        @Override
        public ProductVariantDetailViewModel createFromParcel(Parcel source) {
            return new ProductVariantDetailViewModel(source);
        }

        @Override
        public ProductVariantDetailViewModel[] newArray(int size) {
            return new ProductVariantDetailViewModel[size];
        }
    };

    @Override
    public String getItemId() {
        return String.valueOf(valueId);
    }
}