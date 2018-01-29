package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class ShipmentItemData implements Parcelable {
    private String id;
    private String type;
    private String priceRange;
    private String deliveryTimeRange;
    private List<CourierItemData> courierItemData;

    public ShipmentItemData() {
    }

    protected ShipmentItemData(Parcel in) {
        id = in.readString();
        type = in.readString();
        priceRange = in.readString();
        deliveryTimeRange = in.readString();
        courierItemData = in.createTypedArrayList(CourierItemData.CREATOR);
    }

    public static final Creator<ShipmentItemData> CREATOR = new Creator<ShipmentItemData>() {
        @Override
        public ShipmentItemData createFromParcel(Parcel in) {
            return new ShipmentItemData(in);
        }

        @Override
        public ShipmentItemData[] newArray(int size) {
            return new ShipmentItemData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getDeliveryTimeRange() {
        return deliveryTimeRange;
    }

    public void setDeliveryTimeRange(String deliveryTimeRange) {
        this.deliveryTimeRange = deliveryTimeRange;
    }

    public List<CourierItemData> getCourierItemData() {
        return courierItemData;
    }

    public void setCourierItemData(List<CourierItemData> courierItemData) {
        this.courierItemData = courierItemData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(priceRange);
        dest.writeString(deliveryTimeRange);
        dest.writeTypedList(courierItemData);
    }
}
