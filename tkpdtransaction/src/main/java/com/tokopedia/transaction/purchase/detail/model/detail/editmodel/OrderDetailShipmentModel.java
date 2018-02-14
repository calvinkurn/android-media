package com.tokopedia.transaction.purchase.detail.model.detail.editmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 12/29/17. Tokopedia
 */

public class OrderDetailShipmentModel implements Parcelable{

    private String orderId;

    private String shippingRef;

    private String shipmentId;

    private String shipmentName;

    private String packageId;

    private String packageName;

    private int orderStatusCode;

    public OrderDetailShipmentModel() {
    }

    protected OrderDetailShipmentModel(Parcel in) {
        orderId = in.readString();
        shippingRef = in.readString();
        shipmentId = in.readString();
        shipmentName = in.readString();
        packageId = in.readString();
        packageName = in.readString();
        orderStatusCode = in.readInt();
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(int orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(shippingRef);
        parcel.writeString(shipmentId);
        parcel.writeString(shipmentName);
        parcel.writeString(packageId);
        parcel.writeString(packageName);
        parcel.writeInt(orderStatusCode);
    }
}
