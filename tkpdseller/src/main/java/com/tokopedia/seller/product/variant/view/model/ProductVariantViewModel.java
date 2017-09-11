package com.tokopedia.seller.product.variant.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.PictureItem;

import java.util.List;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantViewModel implements ItemPickerType, Parcelable {

    public final static int TYPE = 123;

    private long temporaryId;
    private long unitValueId;
    private String hexCode;
    private String title;
    private String icon;

    public void setTemporaryId(long tempId) {
        this.temporaryId = tempId;
    }

    public long getTemporaryId() {
        return temporaryId;
    }

    @Override
    public String getId() {
        return String.valueOf(temporaryId);
    }

    public long getUnitValueId() {
        return unitValueId;
    }

    public void setUnitValueId(long unitValueId) {
        this.unitValueId = unitValueId;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
        dest.writeLong(this.temporaryId);
        dest.writeLong(this.unitValueId);
        dest.writeString(this.hexCode);
        dest.writeString(this.title);
        dest.writeString(this.icon);
    }

    public ProductVariantViewModel() {
    }

    protected ProductVariantViewModel(Parcel in) {
        this.temporaryId = in.readLong();
        this.unitValueId = in.readLong();
        this.hexCode = in.readString();
        this.title = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<ProductVariantViewModel> CREATOR = new Parcelable.Creator<ProductVariantViewModel>() {
        @Override
        public ProductVariantViewModel createFromParcel(Parcel source) {
            return new ProductVariantViewModel(source);
        }

        @Override
        public ProductVariantViewModel[] newArray(int size) {
            return new ProductVariantViewModel[size];
        }
    };
}