package com.tokopedia.transaction.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShipmentFeeBannerModel implements Parcelable {

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

    public ShipmentFeeBannerModel() {
    }

    protected ShipmentFeeBannerModel(Parcel in) {
        this.visible = in.readByte() != 0;
        this.shipmentFeeDiscount = in.readString();
    }

    public static final Parcelable.Creator<ShipmentFeeBannerModel> CREATOR = new Parcelable.Creator<ShipmentFeeBannerModel>() {
        @Override
        public ShipmentFeeBannerModel createFromParcel(Parcel source) {
            return new ShipmentFeeBannerModel(source);
        }

        @Override
        public ShipmentFeeBannerModel[] newArray(int size) {
            return new ShipmentFeeBannerModel[size];
        }
    };
}
