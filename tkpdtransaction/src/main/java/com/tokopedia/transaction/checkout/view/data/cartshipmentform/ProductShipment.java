package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ProductShipment implements Parcelable {
    private String shipmentId;
    private List<String> serviceId = new ArrayList<>();

    public String getShipmentId() {
        return shipmentId;
    }

    public List<String> getServiceId() {
        return serviceId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public void setServiceId(List<String> serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shipmentId);
        dest.writeStringList(this.serviceId);
    }

    public ProductShipment() {
    }

    protected ProductShipment(Parcel in) {
        this.shipmentId = in.readString();
        this.serviceId = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ProductShipment> CREATOR = new Parcelable.Creator<ProductShipment>() {
        @Override
        public ProductShipment createFromParcel(Parcel source) {
            return new ProductShipment(source);
        }

        @Override
        public ProductShipment[] newArray(int size) {
            return new ProductShipment[size];
        }
    };
}
