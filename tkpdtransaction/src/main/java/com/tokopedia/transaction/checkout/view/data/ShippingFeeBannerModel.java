package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShippingFeeBannerModel implements Parcelable {

    private boolean visible;
    private String shipmentFeeDiscount;

    public String getShipmentFeeDiscount() {
        return shipmentFeeDiscount;
    }

    public void setShipmentFeeDiscount(String shipmentFeeDiscount) {
        this.shipmentFeeDiscount = shipmentFeeDiscount;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.visible ? (byte) 1 : (byte) 0);
        dest.writeString(this.shipmentFeeDiscount);
    }

    public ShippingFeeBannerModel() {
    }

    protected ShippingFeeBannerModel(Parcel in) {
        this.visible = in.readByte() != 0;
        this.shipmentFeeDiscount = in.readString();
    }

    public static final Parcelable.Creator<ShippingFeeBannerModel> CREATOR = new Parcelable.Creator<ShippingFeeBannerModel>() {
        @Override
        public ShippingFeeBannerModel createFromParcel(Parcel source) {
            return new ShippingFeeBannerModel(source);
        }

        @Override
        public ShippingFeeBannerModel[] newArray(int size) {
            return new ShippingFeeBannerModel[size];
        }
    };
}
