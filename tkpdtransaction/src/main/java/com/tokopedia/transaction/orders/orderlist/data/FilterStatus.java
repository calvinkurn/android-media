package com.tokopedia.transaction.orders.orderlist.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterStatus implements Parcelable {

    @SerializedName("label")
    @Expose
    private String filterLabel;

    @SerializedName("value")
    @Expose
    private String filterName;

    protected FilterStatus(Parcel in) {
        filterLabel = in.readString();
        filterName = in.readString();
    }

    public static final Creator<FilterStatus> CREATOR = new Creator<FilterStatus>() {
        @Override
        public FilterStatus createFromParcel(Parcel in) {
            return new FilterStatus(in);
        }

        @Override
        public FilterStatus[] newArray(int size) {
            return new FilterStatus[size];
        }
    };

    public String getFilterLabel() {
        return filterLabel;
    }

    public void setFilterLabel(String filterLabel) {
        this.filterLabel = filterLabel;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(filterLabel);
        parcel.writeString(filterName);
    }
}
