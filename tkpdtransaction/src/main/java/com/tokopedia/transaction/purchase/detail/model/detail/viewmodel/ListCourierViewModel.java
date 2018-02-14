package com.tokopedia.transaction.purchase.detail.model.detail.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class ListCourierViewModel implements Parcelable {

    private List<CourierViewModel> courierViewModelList;

    public ListCourierViewModel() {
    }

    protected ListCourierViewModel(Parcel in) {
        courierViewModelList = in.createTypedArrayList(CourierViewModel.CREATOR);
    }

    public static final Creator<ListCourierViewModel> CREATOR = new Creator<ListCourierViewModel>() {
        @Override
        public ListCourierViewModel createFromParcel(Parcel in) {
            return new ListCourierViewModel(in);
        }

        @Override
        public ListCourierViewModel[] newArray(int size) {
            return new ListCourierViewModel[size];
        }
    };

    public List<CourierViewModel> getCourierViewModelList() {
        return courierViewModelList;
    }

    public void setCourierViewModelList(List<CourierViewModel> courierViewModelList) {
        this.courierViewModelList = courierViewModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(courierViewModelList);
    }
}
