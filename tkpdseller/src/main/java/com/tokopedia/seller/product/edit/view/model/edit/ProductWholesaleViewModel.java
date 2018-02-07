
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductWholesaleViewModel implements Parcelable {

    @SerializedName("min_qty")
    @Expose
    private long minQty;
    @SerializedName("price_value")
    @Expose
    private double priceValue;

    public long getMinQty() {
        return minQty;
    }

    public void setMinQty(long minQty) {
        this.minQty = minQty;
    }

    public double getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(double priceValue) {
        this.priceValue = priceValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.minQty);
        dest.writeDouble(this.priceValue);
    }

    public ProductWholesaleViewModel() {
    }

    protected ProductWholesaleViewModel(Parcel in) {
        this.minQty = in.readLong();
        this.priceValue = in.readDouble();
    }

    public static final Parcelable.Creator<ProductWholesaleViewModel> CREATOR = new Parcelable.Creator<ProductWholesaleViewModel>() {
        @Override
        public ProductWholesaleViewModel createFromParcel(Parcel source) {
            return new ProductWholesaleViewModel(source);
        }

        @Override
        public ProductWholesaleViewModel[] newArray(int size) {
            return new ProductWholesaleViewModel[size];
        }
    };
}
