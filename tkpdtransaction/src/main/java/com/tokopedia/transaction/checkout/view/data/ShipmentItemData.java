package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class ShipmentItemData implements Parcelable {
    private int serviceId;
    private String type;
    private String singlePriceRange;
    private String multiplePriceRange;
    private String deliveryTimeRange;
    private List<CourierItemData> courierItemData;
    private boolean selected;

    public ShipmentItemData() {
    }

    protected ShipmentItemData(Parcel in) {
        serviceId = in.readInt();
        type = in.readString();
        singlePriceRange = in.readString();
        deliveryTimeRange = in.readString();
        multiplePriceRange = in.readString();
        courierItemData = in.createTypedArrayList(CourierItemData.CREATOR);
        selected = in.readByte() != 0;
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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int id) {
        this.serviceId = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSinglePriceRange() {
        return singlePriceRange;
    }

    public void setSinglePriceRange(String singlePriceRange) {
        this.singlePriceRange = singlePriceRange;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMultiplePriceRange() {
        return multiplePriceRange;
    }

    public void setMultiplePriceRange(String multiplePriceRange) {
        this.multiplePriceRange = multiplePriceRange;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(serviceId);
        dest.writeString(type);
        dest.writeString(singlePriceRange);
        dest.writeString(multiplePriceRange);
        dest.writeString(deliveryTimeRange);
        dest.writeTypedList(courierItemData);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
