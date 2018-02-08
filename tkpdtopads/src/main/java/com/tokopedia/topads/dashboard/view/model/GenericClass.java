package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by normansyahputa on 2/22/17.
 */

public class GenericClass implements Parcelable {
    public static final Parcelable.Creator<GenericClass> CREATOR = new Parcelable.Creator<GenericClass>() {
        @Override
        public GenericClass createFromParcel(Parcel source) {
            return new GenericClass(source);
        }

        @Override
        public GenericClass[] newArray(int size) {
            return new GenericClass[size];
        }
    };
    private String className;

    public GenericClass(String className) {
        this.className = className;
    }

    protected GenericClass(Parcel in) {
        this.className = in.readString();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.className);
    }
}
