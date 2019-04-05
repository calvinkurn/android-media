package com.tokopedia.transaction.orders.orderlist.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OrderLabelList implements Parcelable {

    @SerializedName("labelBhasa")
    @Expose
    String labelBhasa;


    @SerializedName("orderCategory")
    @Expose

    String orderCategory;

    @SerializedName("status")
    @Expose
    private List<FilterStatus> filterStatusList = new ArrayList<>();

    protected OrderLabelList(Parcel in) {
        labelBhasa = in.readString();
        orderCategory = in.readString();
        filterStatusList = in.createTypedArrayList(FilterStatus.CREATOR);
    }

    public static final Creator<OrderLabelList> CREATOR = new Creator<OrderLabelList>() {
        @Override
        public OrderLabelList createFromParcel(Parcel in) {
            return new OrderLabelList(in);
        }

        @Override
        public OrderLabelList[] newArray(int size) {
            return new OrderLabelList[size];
        }
    };

    public String getLabelBhasa() {
        return labelBhasa;
    }


    public String getOrderCategory() {
        return orderCategory;
    }

    public List<FilterStatus> getFilterStatusList() {
        return filterStatusList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(labelBhasa);
        parcel.writeString(orderCategory);
        parcel.writeTypedList(filterStatusList);
    }
}