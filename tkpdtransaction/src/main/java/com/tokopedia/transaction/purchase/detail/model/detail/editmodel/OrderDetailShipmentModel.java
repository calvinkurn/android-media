package com.tokopedia.transaction.purchase.detail.model.detail.editmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 12/29/17. Tokopedia
 */

public class OrderDetailShipmentModel implements Parcelable{

    private String shippingRef;

    private String shipmentId;

    private String shipmentName;

    private String packageId;

    protected OrderDetailShipmentModel(Parcel in) {
        shippingRef = in.readString();
        shipmentId = in.readString();
        shipmentName = in.readString();
        packageId = in.readString();
    }

    public static final Creator<OrderDetailShipmentModel> CREATOR = new Creator<OrderDetailShipmentModel>() {
        @Override
        public OrderDetailShipmentModel createFromParcel(Parcel in) {
            return new OrderDetailShipmentModel(in);
        }

        @Override
        public OrderDetailShipmentModel[] newArray(int size) {
            return new OrderDetailShipmentModel[size];
        }
    };

    public String getShippingRef() {
        return shippingRef;
    }

    public void setShippingRef(String shippinRef) {
        this.shippingRef = shippinRef;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shippingRef);
        parcel.writeString(shipmentId);
        parcel.writeString(shipmentName);
        parcel.writeString(packageId);
    }
}
