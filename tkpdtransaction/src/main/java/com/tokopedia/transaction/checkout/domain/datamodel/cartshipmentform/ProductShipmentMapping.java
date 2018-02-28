package com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ProductShipmentMapping implements Parcelable {
    private String shipmentId;
    private String shippingIds;
    private List<ServiceId> serviceIds = new ArrayList<>();

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public void setShippingIds(String shippingIds) {
        this.shippingIds = shippingIds;
    }

    public void setServiceIds(List<ServiceId> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public String getShippingIds() {
        return shippingIds;
    }

    public List<ServiceId> getServiceIds() {
        return serviceIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shipmentId);
        dest.writeString(this.shippingIds);
        dest.writeTypedList(this.serviceIds);
    }

    public ProductShipmentMapping() {
    }

    protected ProductShipmentMapping(Parcel in) {
        this.shipmentId = in.readString();
        this.shippingIds = in.readString();
        this.serviceIds = in.createTypedArrayList(ServiceId.CREATOR);
    }

    public static final Parcelable.Creator<ProductShipmentMapping> CREATOR = new Parcelable.Creator<ProductShipmentMapping>() {
        @Override
        public ProductShipmentMapping createFromParcel(Parcel source) {
            return new ProductShipmentMapping(source);
        }

        @Override
        public ProductShipmentMapping[] newArray(int size) {
            return new ProductShipmentMapping[size];
        }
    };
}
