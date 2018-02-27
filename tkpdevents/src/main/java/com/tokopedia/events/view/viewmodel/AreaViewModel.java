package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class AreaViewModel implements Parcelable {


    private String id;
    private String description;
    private String areaCode;
    private int areaNo;
    private String isSelected;
    private int seatReservedCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(int areaNo) {
        this.areaNo = areaNo;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public int getSeatReservedCount() {
        return seatReservedCount;
    }

    public void setSeatReservedCount(int seatReservedCount) {
        this.seatReservedCount = seatReservedCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.areaCode);
        dest.writeInt(this.areaNo);
        dest.writeString(this.isSelected);
        dest.writeInt(this.seatReservedCount);
    }

    public AreaViewModel() {
    }

    protected AreaViewModel(Parcel in) {
        this.id = in.readString();
        this.description = in.readString();
        this.areaCode = in.readString();
        this.areaNo = in.readInt();
        this.isSelected = in.readString();
        this.seatReservedCount = in.readInt();
    }

    public static final Parcelable.Creator<AreaViewModel> CREATOR = new Parcelable.Creator<AreaViewModel>() {
        @Override
        public AreaViewModel createFromParcel(Parcel source) {
            return new AreaViewModel(source);
        }

        @Override
        public AreaViewModel[] newArray(int size) {
            return new AreaViewModel[size];
        }
    };
}
