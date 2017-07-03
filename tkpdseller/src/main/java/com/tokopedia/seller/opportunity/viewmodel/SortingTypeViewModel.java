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

    protected SortingTypeViewModel(Parcel in) {
        value = in.readString();
        name = in.readString();
        key = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<SortingTypeViewModel> CREATOR = new Creator<SortingTypeViewModel>() {
        @Override
        public SortingTypeViewModel createFromParcel(Parcel in) {
            return new SortingTypeViewModel(in);
        }

        @Override
        public SortingTypeViewModel[] newArray(int size) {
            return new SortingTypeViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeString(name);
        dest.writeString(key);
        dest.writeByte((byte) (isSelected ? 1 : 0));
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
}
