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
    private String price;
    private String title;
    private int priceNumeric;

    protected FlightBookingLuggageViewModel(Parcel in) {
        id = in.readString();
        price = in.readString();
        title = in.readString();
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



    public FlightBookingLuggageViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title + " - " + price;
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
        parcel.writeString(price);
        parcel.writeString(title);
        parcel.writeInt(priceNumeric);
    }
}
