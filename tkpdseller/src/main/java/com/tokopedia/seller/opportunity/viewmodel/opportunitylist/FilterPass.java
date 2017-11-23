package com.tokopedia.seller.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 4/27/17.
 */

public class FilterPass implements Parcelable{
    private String key;
    private String value;
    private String name;

    public FilterPass(){}

    public FilterPass(String key, String value, String name) {
        this.key = key;
        this.value = value;
        this.name = name;
    }

    public FilterPass(Parcel in) {
        key = in.readString();
        value = in.readString();
        name = in.readString();
    }

    public static final Creator<FilterPass> CREATOR = new Creator<FilterPass>() {
        @Override
        public FilterPass createFromParcel(Parcel in) {
            return new FilterPass(in);
        }

        @Override
        public FilterPass[] newArray(int size) {
            return new FilterPass[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
        dest.writeString(name);
    }
}
