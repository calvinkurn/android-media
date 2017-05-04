package com.tokopedia.seller.product.view.model.upload;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductWholesaleViewModel implements Parcelable {
    private int qtyMin;
    private int qtyMax;
    private double price;

    public int getQtyMin() {
        return qtyMin;
    }

    public int getQtyMax() {
        return qtyMax;
    }

    public double getPrice() {
        return price;
    }

    public void setQtyMin(int qtyMin) {
        this.qtyMin = qtyMin;
    }

    public void setQtyMax(int qtyMax) {
        this.qtyMax = qtyMax;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.qtyMin);
        dest.writeInt(this.qtyMax);
        dest.writeDouble(this.price);
    }

    public ProductWholesaleViewModel() {
    }

    protected ProductWholesaleViewModel(Parcel in) {
        this.qtyMin = in.readInt();
        this.qtyMax = in.readInt();
        this.price = in.readDouble();
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
