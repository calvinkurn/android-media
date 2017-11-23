package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.type.ItemIdType;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingMealViewModel implements Parcelable, ItemType, ItemIdType {
    public static final int TYPE = 532;
    private String id;
    private String title;
    private String price;

    public FlightBookingMealViewModel() {
    }

    protected FlightBookingMealViewModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        price = in.readString();
    }

    public static final Creator<FlightBookingMealViewModel> CREATOR = new Creator<FlightBookingMealViewModel>() {
        @Override
        public FlightBookingMealViewModel createFromParcel(Parcel in) {
            return new FlightBookingMealViewModel(in);
        }

        @Override
        public FlightBookingMealViewModel[] newArray(int size) {
            return new FlightBookingMealViewModel[size];
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(price);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingMealViewModel && ((FlightBookingMealViewModel) obj).getId().equalsIgnoreCase(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
