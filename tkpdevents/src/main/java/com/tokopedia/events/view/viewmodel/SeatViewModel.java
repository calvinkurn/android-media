package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatViewModel implements Parcelable {

    private String areaId;
    private int no;
    private int status;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SeatViewModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.areaId);
        dest.writeValue(this.no);
        dest.writeValue(this.status);
    }

    protected SeatViewModel(Parcel in) {
        this.areaId = in.readString();
        this.no = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<SeatViewModel> CREATOR = new Creator<SeatViewModel>() {
        @Override
        public SeatViewModel createFromParcel(Parcel source) {
            return new SeatViewModel(source);
        }

        @Override
        public SeatViewModel[] newArray(int size) {
            return new SeatViewModel[size];
        }
    };
}
