package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/21/17.
 */


public class FilterPass implements Parcelable {
    private String key;
    private String value;

    public FilterPass() {
    }

    public FilterPass(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public FilterPass(Parcel in) {
        key = in.readString();
        value = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }
}