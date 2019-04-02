package com.tokopedia.seller.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/6/17.
 */

public class SortingTypeViewModel implements Parcelable {
    private String value;
    private String name;
    private String key;
    private boolean isSelected;

    public SortingTypeViewModel() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.value);
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected SortingTypeViewModel(Parcel in) {
        this.value = in.readString();
        this.name = in.readString();
        this.key = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<SortingTypeViewModel> CREATOR = new Creator<SortingTypeViewModel>() {
        @Override
        public SortingTypeViewModel createFromParcel(Parcel source) {
            return new SortingTypeViewModel(source);
        }

        @Override
        public SortingTypeViewModel[] newArray(int size) {
            return new SortingTypeViewModel[size];
        }
    };
}
