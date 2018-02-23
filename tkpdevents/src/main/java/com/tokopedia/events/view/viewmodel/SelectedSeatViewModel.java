package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by naveengoyal on 1/30/18.
 */

public class SelectedSeatViewModel implements Parcelable {


    private int quantity;

    private int price;

    private List<String> areaCodes;

    private List<String> seatIds;

    private List<String> seatRowIds;

    private List<String> physicalRowIds;

    private String areaId;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getAreaCodes() {
        return areaCodes;
    }

    public void setAreaCodes(List<String> areaCodes) {
        this.areaCodes = areaCodes;
    }

    public List<String> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<String> seatIds) {
        this.seatIds = seatIds;
    }

    public List<String> getSeatRowIds() {
        return seatRowIds;
    }

    public void setSeatRowIds(List<String> seatRowIds) {
        this.seatRowIds = seatRowIds;
    }

    public List<String> getPhysicalRowIds() {
        return physicalRowIds;
    }

    public void setPhysicalRowIds(List<String> physicalRowIds) {
        this.physicalRowIds = physicalRowIds;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }


    public SelectedSeatViewModel() {

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.quantity);
        dest.writeValue(this.price);
        dest.writeStringList(this.areaCodes);
        dest.writeStringList(this.seatIds);
        dest.writeStringList(this.seatRowIds);
        dest.writeStringList(this.physicalRowIds);
        dest.writeString(this.areaId);
    }


    protected  SelectedSeatViewModel(Parcel in) {
        this.quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.price = (Integer) in.readValue(Integer.class.getClassLoader());
        this.areaCodes = in.createStringArrayList();
        this.seatIds = in.createStringArrayList();
        this.seatRowIds = in.createStringArrayList();
        this.physicalRowIds = in.createStringArrayList();
        this.areaId = in.readString();
    }

    public static final Creator<SelectedSeatViewModel> CREATOR = new Creator<SelectedSeatViewModel>() {
        @Override
        public SelectedSeatViewModel createFromParcel(Parcel source) {
            return new SelectedSeatViewModel(source);
        }

        @Override
        public SelectedSeatViewModel[] newArray(int size) {
            return new SelectedSeatViewModel[size];
        }
    };
}
