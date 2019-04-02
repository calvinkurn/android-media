package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 24/01/18.
 */

public class FilterViewModel implements Parcelable {
    private String type;
    private String typeNameDetail;
    private String typeNameQuickFilter;
    private int count;
    private int orderValue;
    private boolean isActive;

    public FilterViewModel(String type, String typeNameDetail, String typeNameQuickFilter, int count, int orderValue, boolean isActive) {
        this.type = type;
        this.typeNameDetail = typeNameDetail;
        this.typeNameQuickFilter = typeNameQuickFilter;
        this.count = count;
        this.orderValue = orderValue;
        this.isActive = isActive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeNameDetail() {
        return typeNameDetail;
    }

    public void setTypeNameDetail(String typeNameDetail) {
        this.typeNameDetail = typeNameDetail;
    }

    public String getTypeNameQuickFilter() {
        return typeNameQuickFilter;
    }

    public void setTypeNameQuickFilter(String typeNameQuickFilter) {
        this.typeNameQuickFilter = typeNameQuickFilter;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.typeNameDetail);
        dest.writeString(this.typeNameQuickFilter);
        dest.writeInt(this.count);
        dest.writeInt(this.orderValue);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
    }

    protected FilterViewModel(Parcel in) {
        this.type = in.readString();
        this.typeNameDetail = in.readString();
        this.typeNameQuickFilter = in.readString();
        this.count = in.readInt();
        this.orderValue = in.readInt();
        this.isActive = in.readByte() != 0;
    }

    public static final Creator<FilterViewModel> CREATOR = new Creator<FilterViewModel>() {
        @Override
        public FilterViewModel createFromParcel(Parcel source) {
            return new FilterViewModel(source);
        }

        @Override
        public FilterViewModel[] newArray(int size) {
            return new FilterViewModel[size];
        }
    };
}
