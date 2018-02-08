package com.tokopedia.discovery.newdiscovery.hotlist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 10/8/17.
 */

public class HotlistHashTagViewModel implements Parcelable {
    private String departmentID;
    private String name;
    private String URL;

    public HotlistHashTagViewModel() {
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.departmentID);
        dest.writeString(this.name);
        dest.writeString(this.URL);
    }

    protected HotlistHashTagViewModel(Parcel in) {
        this.departmentID = in.readString();
        this.name = in.readString();
        this.URL = in.readString();
    }

    public static final Parcelable.Creator<HotlistHashTagViewModel> CREATOR = new Parcelable.Creator<HotlistHashTagViewModel>() {
        @Override
        public HotlistHashTagViewModel createFromParcel(Parcel source) {
            return new HotlistHashTagViewModel(source);
        }

        @Override
        public HotlistHashTagViewModel[] newArray(int size) {
            return new HotlistHashTagViewModel[size];
        }
    };
}
