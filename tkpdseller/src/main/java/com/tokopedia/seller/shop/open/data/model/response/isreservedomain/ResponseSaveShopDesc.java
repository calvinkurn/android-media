package com.tokopedia.seller.shop.open.data.model.response.isreservedomain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSaveShopDesc implements Parcelable {

    @SerializedName("reserve_status")
    @Expose
    private long reserveStatus;
    @SerializedName("shipment")
    @Expose
    private Shipment shipment;
    @SerializedName("user_data")
    @Expose
    private UserData userData;

    public long getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(long reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.reserveStatus);
        dest.writeParcelable(this.shipment, flags);
        dest.writeParcelable(this.userData, flags);
    }

    public ResponseSaveShopDesc() {
    }

    protected ResponseSaveShopDesc(Parcel in) {
        this.reserveStatus = in.readLong();
        this.shipment = in.readParcelable(Shipment.class.getClassLoader());
        this.userData = in.readParcelable(UserData.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResponseSaveShopDesc> CREATOR = new Parcelable.Creator<ResponseSaveShopDesc>() {
        @Override
        public ResponseSaveShopDesc createFromParcel(Parcel source) {
            return new ResponseSaveShopDesc(source);
        }

        @Override
        public ResponseSaveShopDesc[] newArray(int size) {
            return new ResponseSaveShopDesc[size];
        }
    };

    public boolean isDomainAlreadyReserved() {
        return reserveStatus != 0;
    }
}