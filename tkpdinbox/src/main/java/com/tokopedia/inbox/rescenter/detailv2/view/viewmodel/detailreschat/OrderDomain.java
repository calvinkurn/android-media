package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class OrderDomain implements Parcelable {
    public static final Parcelable.Creator<OrderDomain> CREATOR = new Parcelable.Creator<OrderDomain>() {
        @Override
        public OrderDomain createFromParcel(Parcel source) {
            return new OrderDomain(source);
        }

        @Override
        public OrderDomain[] newArray(int size) {
            return new OrderDomain[size];
        }
    };
    private int openAmount;
    private int shippingPrices;

    public OrderDomain(int openAmount, int shippingPrices) {
        this.openAmount = openAmount;
        this.shippingPrices = shippingPrices;
    }

    protected OrderDomain(Parcel in) {
        this.openAmount = in.readInt();
        this.shippingPrices = in.readInt();
    }

    public int getOpenAmount() {
        return openAmount;
    }

    public void setOpenAmount(int openAmount) {
        this.openAmount = openAmount;
    }

    public int getShippingPrices() {
        return shippingPrices;
    }

    public void setShippingPrices(int shippingPrices) {
        this.shippingPrices = shippingPrices;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.openAmount);
        dest.writeInt(this.shippingPrices);
    }
}
