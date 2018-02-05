package com.tokopedia.movies.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class AreaViewModel{


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
}
