package com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 22/02/18.
 */

public class CartShipmentProduct implements Parcelable {
    private int shipmentProductId;
    private int additionalFee;

    protected CartShipmentProduct(Parcel in) {
        shipmentProductId = in.readInt();
        additionalFee = in.readInt();
    }

    public static final Creator<CartShipmentProduct> CREATOR = new Creator<CartShipmentProduct>() {
        @Override
        public CartShipmentProduct createFromParcel(Parcel in) {
            return new CartShipmentProduct(in);
        }

        @Override
        public CartShipmentProduct[] newArray(int size) {
            return new CartShipmentProduct[size];
        }
    };

    public int getShipmentProductId() {
        return shipmentProductId;
    }

    public void setShipmentProductId(int shipmentProductId) {
        this.shipmentProductId = shipmentProductId;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(int additionalFee) {
        this.additionalFee = additionalFee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shipmentProductId);
        dest.writeInt(additionalFee);
    }
}
