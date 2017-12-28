package com.tokopedia.seller.shop.open.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class CourierServiceIdWrapper implements Parcelable {
    private List<String> selectedServiceIdList = new ArrayList<>();
    private List<CourierServiceId> courierServiceIdList = new ArrayList<>();

    public List<String> getSelectedServiceIdList() {
        return selectedServiceIdList;
    }

    public List<CourierServiceId> getCourierServiceIdList() {
        return courierServiceIdList;
    }

    public boolean contains(String courierId) {
        return selectedServiceIdList.contains(courierId);
    }

    public void add(String courierId, List<String> selectedCourierServiceId) {
        selectedServiceIdList.add(courierId);
        courierServiceIdList.add(new CourierServiceId(courierId, selectedCourierServiceId));
    }

    public List<String> getCourierServiceIdList(String courierId) {
        for (int i = 0, sizei = courierServiceIdList.size(); i < sizei; i++) {
            if (courierId.equals(courierServiceIdList.get(i).getCourierID())){
                return courierServiceIdList.get(i).getCourierServiceIdList();
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.selectedServiceIdList);
        dest.writeTypedList(this.courierServiceIdList);
    }

    public CourierServiceIdWrapper() {
    }

    protected CourierServiceIdWrapper(Parcel in) {
        this.selectedServiceIdList = in.createStringArrayList();
        this.courierServiceIdList = in.createTypedArrayList(CourierServiceId.CREATOR);
    }

    public static final Parcelable.Creator<CourierServiceIdWrapper> CREATOR = new Parcelable.Creator<CourierServiceIdWrapper>() {
        @Override
        public CourierServiceIdWrapper createFromParcel(Parcel source) {
            return new CourierServiceIdWrapper(source);
        }

        @Override
        public CourierServiceIdWrapper[] newArray(int size) {
            return new CourierServiceIdWrapper[size];
        }
    };
}
