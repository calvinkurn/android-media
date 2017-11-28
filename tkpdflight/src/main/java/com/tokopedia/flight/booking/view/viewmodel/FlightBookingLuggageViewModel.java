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

    protected FlightBookingLuggageViewModel(Parcel in) {
        id = in.readString();
        priceFmt = in.readString();
        weightFmt = in.readString();
        priceNumeric = in.readInt();
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

    public int getPriceNumeric() {
        return priceNumeric;
    }

    public void setPriceNumeric(int priceNumeric) {
        this.priceNumeric = priceNumeric;
    }

    private int priceNumeric;

    public FlightBookingLuggageViewModel() {
    }

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
    public String toString() {
        return weightFmt + " - " + priceFmt;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingLuggageViewModel &&
                ((FlightBookingLuggageViewModel) obj).getId().equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(priceFmt);
        parcel.writeString(weightFmt);
        parcel.writeInt(priceNumeric);
    }
}
