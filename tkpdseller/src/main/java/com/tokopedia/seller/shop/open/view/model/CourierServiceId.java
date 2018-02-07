package com.tokopedia.seller.shop.open.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CourierServiceId implements Parcelable {
    private String courierID;
    private List<String> courierServiceIdList;

    public CourierServiceId(String courierID, List<String> courierServiceIdList) {
        this.courierID = courierID;
        this.courierServiceIdList = courierServiceIdList;
    }

    public String getCourierID() {
        return courierID;
    }

    public List<String> getCourierServiceIdList() {
        return courierServiceIdList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courierID);
        dest.writeStringList(this.courierServiceIdList);
    }

    public CourierServiceId() {
    }

    protected CourierServiceId(Parcel in) {
        this.courierID = in.readString();
        this.courierServiceIdList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<CourierServiceId> CREATOR = new Parcelable.Creator<CourierServiceId>() {
        @Override
        public CourierServiceId createFromParcel(Parcel source) {
            return new CourierServiceId(source);
        }

        @Override
        public CourierServiceId[] newArray(int size) {
            return new CourierServiceId[size];
        }
    };
}
