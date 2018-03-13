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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.areaId);
        dest.writeInt(this.no);
        dest.writeInt(this.status);
    }

    public SeatViewModel() {
    }

    protected SeatViewModel(Parcel in) {
        this.areaId = in.readString();
        this.no = in.readInt();
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<SeatViewModel> CREATOR = new Parcelable.Creator<SeatViewModel>() {
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
