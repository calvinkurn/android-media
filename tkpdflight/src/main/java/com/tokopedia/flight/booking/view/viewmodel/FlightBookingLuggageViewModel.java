package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingLuggageViewModel implements Parcelable, ItemType {
    public static final int TYPE = 7923;
    private String id;
    private String priceFmt;
    private String weightFmt;

    public FlightBookingLuggageViewModel() {
    }

    protected FlightBookingLuggageViewModel(Parcel in) {
        id = in.readString();
        priceFmt = in.readString();
        weightFmt = in.readString();
    }

    public static final Creator<FlightBookingLuggageViewModel> CREATOR = new Creator<FlightBookingLuggageViewModel>() {
        @Override
        public FlightBookingLuggageViewModel createFromParcel(Parcel in) {
            return new FlightBookingLuggageViewModel(in);
        }

        @Override
        public FlightBookingLuggageViewModel[] newArray(int size) {
            return new FlightBookingLuggageViewModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriceFmt() {
        return priceFmt;
    }

    public void setPriceFmt(String priceFmt) {
        this.priceFmt = priceFmt;
    }

    public String getWeightFmt() {
        return weightFmt;
    }

    public void setWeightFmt(String weightFmt) {
        this.weightFmt = weightFmt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return weightFmt;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(priceFmt);
        dest.writeString(weightFmt);
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
