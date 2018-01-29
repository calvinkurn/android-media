package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class CourierItemData implements Parcelable {
    private String id;
    private String name;
    private String price;
    private String deliveryTimeRange;
    private String deliverySchedule;
    private boolean selected;

    public CourierItemData() {
    }

    protected CourierItemData(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        deliveryTimeRange = in.readString();
        deliverySchedule = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<CourierItemData> CREATOR = new Creator<CourierItemData>() {
        @Override
        public CourierItemData createFromParcel(Parcel in) {
            return new CourierItemData(in);
        }

        @Override
        public CourierItemData[] newArray(int size) {
            return new CourierItemData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeliveryTimeRange() {
        return deliveryTimeRange;
    }

    public void setDeliveryTimeRange(String deliveryTimeRange) {
        this.deliveryTimeRange = deliveryTimeRange;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDeliverySchedule() {
        return deliverySchedule;
    }

    public void setDeliverySchedule(String deliverySchedule) {
        this.deliverySchedule = deliverySchedule;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(deliveryTimeRange);
        dest.writeString(deliverySchedule);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
