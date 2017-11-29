package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.type.ItemIdType;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingAmenityViewModel implements Parcelable, ItemType, ItemIdType {
    public static final int TYPE = 532;
    private String id;
    private String title;
    private String price;
    private int priceNumeric;

    public FlightBookingAmenityViewModel() {
    }

    protected FlightBookingAmenityViewModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        price = in.readString();
        priceNumeric = in.readInt();
    }

    public static final Creator<FlightBookingAmenityViewModel> CREATOR = new Creator<FlightBookingAmenityViewModel>() {
        @Override
        public FlightBookingAmenityViewModel createFromParcel(Parcel in) {
            return new FlightBookingAmenityViewModel(in);
        }

        @Override
        public FlightBookingAmenityViewModel[] newArray(int size) {
            return new FlightBookingAmenityViewModel[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingAmenityViewModel && ((FlightBookingAmenityViewModel) obj).getId().equalsIgnoreCase(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int getPriceNumeric() {
        return priceNumeric;
    }

    public void setPriceNumeric(int priceNumeric) {
        this.priceNumeric = priceNumeric;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(price);
        parcel.writeInt(priceNumeric);
    }
}
