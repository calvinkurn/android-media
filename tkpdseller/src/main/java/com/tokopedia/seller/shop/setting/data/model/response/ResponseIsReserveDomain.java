package com.tokopedia.seller.shop.setting.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class ResponseIsReserveDomain implements Parcelable {

    @SerializedName("ServerProcessTime")
    @Expose
    private String serverProcessTime;
    @SerializedName("reserve_status")
    @Expose
    private int reserveStatus;
    @SerializedName("shipment")
    @Expose
    private Shipment shipment;
    @SerializedName("user_data")
    @Expose
    private UserData userData;

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public int getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(int reserveStatus) {
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
    public boolean isDomainAlreadyReserved(){
        return reserveStatus!= 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serverProcessTime);
        dest.writeInt(this.reserveStatus);
        dest.writeParcelable(this.shipment, flags);
        dest.writeParcelable(this.userData, flags);
    }

    public ResponseIsReserveDomain() {
    }

    protected ResponseIsReserveDomain(Parcel in) {
        this.serverProcessTime = in.readString();
        this.reserveStatus = in.readInt();
        this.shipment = in.readParcelable(Shipment.class.getClassLoader());
        this.userData = in.readParcelable(UserData.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResponseIsReserveDomain> CREATOR = new Parcelable.Creator<ResponseIsReserveDomain>() {
        @Override
        public ResponseIsReserveDomain createFromParcel(Parcel source) {
            return new ResponseIsReserveDomain(source);
        }

        @Override
        public ResponseIsReserveDomain[] newArray(int size) {
            return new ResponseIsReserveDomain[size];
        }
    };
}
